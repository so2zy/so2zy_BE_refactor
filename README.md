# Aroom

**여행 여정을 기록과 관리하는 서비스**

## 📢 목차
1. [팀원 소개](🧑‍🤝‍🧑-팀원-소개)
2. [프로젝트 소개](📽️-프로젝트-소개)
3. [API Document](📄-API-Document)
4. [Release Note](✏️-Release-Note)
5. [ERD](✅-ERD)
6. [에러 해결 방법](💯-에러-해결-방안)
7. [개인 역량 회고](🤖-개인-역량-회고)

## 🧑‍🤝‍🧑 팀원 소개


|                                         Backend                                         |                                        Backend                                         |                                        Backend                                         |                                        Backend                                         |
|:---------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/139187207?v=4" width=130px alt="자현"/> | <img src="https://avatars.githubusercontent.com/u/34360434?v=4" width=130px alt="민우"/> | <img src="https://avatars.githubusercontent.com/u/63856521?v=4" width=130px alt="유림"/> | <img src="https://avatars.githubusercontent.com/u/40655807?v=4" width=130px alt="동민"/> |
|                            [자현](https://github.com/Nine-JH)                             |                          [민우](https://github.com/Kwonminwoo)                           |                           [유림](https://github.com/YurimYang)                           |                          [동민](https://github.com/chadongmin)                           |
|                            로그인/회원가입/찜                            |                          장바구니 조회&삭제/예약                      |                           장바구니 추가/숙소목록 상세 조회                          |                          숙소 전체 조회/검색 조회                          |

## 📽️ 프로젝트 소개
![image](https://github.com/so2zy/so2zy_BE_refactor/assets/63856521/13547e58-ebd2-4c00-8250-57e9b81d083d)

**⏲️ 개발 기간**
* 1차 : 2023.11.10 ~ 2023.11.16
* 2차(리팩토링) : 2023.11.04 ~ 2023.11.15


<br/>

**🔗 배포 사이트**
* https://candid-horse-912de6.netlify.app/


 <br/>     

**🔨 구현 환경**
* Java 17
* Spring Boot 3.1.5
* Mysql 8.0, H2, Redis
* Docker
* Intellij
* gradle
* test - Junit


## 📄 API Document
* https://api.so2zy.com/docs/index.html

## ✏️ Release Note
* [v2.0 release note](https://github.com/so2zy/so2zy_BE/wiki/So2zy-2.0-Release-Notes)
* [v1.0 release note](https://github.com/so2zy/so2zy_BE/wiki/So2zy-1.0-Release-Notes)

## ✅ ERD
![ERD](https://github.com/so2zy/so2zy_BE/assets/139187207/3c2bdb39-d128-4568-a0f7-f61d746e6897)


## 💯 에러 해결 방안
<details>
<summary>에러 내용 및 해결</summary>
 
### 1. StackOverFlow Error 문제

**1 - 1. 원인**

```bash
Infinite recursion (StackOverflowError) 
(through reference chain: com.aroom.domain.room.model.Room["accommodation"]
->com.aroom.domain.accommodation.model.Accommodation["roomList"]
->org.hibernate.collection.spi.PersistentBag[0]
->com.aroom.domain.room.model.Room["accommodation"]
->com.aroom.domain.accommodation.model.Accommodation["roomList"]-
```

현재 양방향 연관관계에 놓여진 Accommodation과 Room에서 무한순환참조가 발생했다.
<br>
**1 - 2. 해결**

- `@OneToMany` `@manytoone`로 인해 순환참조 원인
- `@JsonManagedReference` & `@JsonBackReference` 추가

```java
@JsonManagedReference
@OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
private List<Room> roomList = new ArrayList<>();
```

- `@JsonManagedReference` : 부모 `Entity` → 자식 `Entity`
    - 정상적으로 직렬화를 수행

```java
@JsonBackReference
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "accommodation_id")
private Accommodation accommodation;
```

- `@JsonBackReference` : 자식`Entity` → 부모 `Entity`
    - 직렬화 수행 x
        
        ⇒ 무한 순환 참조 해결
        
<br>

### 2. Jackson 직렬화 제한자 문제

**2 - 1. 발생 과정**

```java
public RoomCartResponseDTO postRoomCart(Long member_id, Long room_id){
    Room room = roomRepository.findById(room_id).get();
    Cart cart = cartRepository.findByMemberId(member_id).get();
    RoomCart roomCart = roomCartRepository.save(new RoomCart(cart,room));
    cart.postRoomCarts(roomCart);
    return new RoomCartResponseDTO(cart);
}
```

```java
@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
private List<RoomCart> roomCartList = new ArrayList<>();
	
public void postRoomCarts(RoomCart roomCart){
	roomCartList.add(roomCart);
}
```

객실을 장바구니에 담을 때 RoomCart를 생성하여 Cart의 List<RoomCart> roomCartList에 post 시도
<br>

**2 - 2. 원인**

```bash
Type definition error: [simple type, class com.aroom.domain.roomCart.dto.response.RoomCartResponseDTO]
```

```bash
org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class com.aroom.domain.roomCart.dto.response.RoomCartResponseDTO]
at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.writeInternal(AbstractJackson2HttpMessageConverter.java:489) ~[spring-web-6.0.13.jar:6.0.13]
at org.springframework.http.converter.AbstractGenericHttpMessageConverter.write(AbstractGenericHttpMessageConverter.java:103) ~[spring-web-6.0.13.jar:6.0.13]
at
```

```bash
caused by: com.fasterxml.jackson.databind.exc.invaliddefinitionexception: 
no serializer found for class com.aroom.domain.roomcart.dto.response.roomcartresponsedto 
and no properties discovered to create beanserializer 
(to avoid exception, disable serializationfeature.fail_on_empty_beans) 
(through reference chain: com.aroom.global.response.apiresponse["data"])
```

- Jackson 라이브러리가 `RoomCartResponseDTO` & `RoomCartInfoDTO`를 직렬화할 때 문제가 발생
- Jackson은 기본적으로 클래스를 직렬화할 때 해당 클래스에 대한 직렬화 메소드를 찾아야 하는데, 여기서는 해당 메소드를 찾지 못했다고 나온다.
- 또한, Jackson은 직렬화 하는 과정에서 기본으로 접근 제한자가 public이거나, getter/setter를 이용하기 때문에 인스턴스 필드를 private등으로 선언시, json으로 변환 과정에서 에러가 발생한다.
<br>

**2 - 3. 해결**

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartResponseDTO {

    private long cart_id;
    private List<RoomCartInfoDTO> roomCartList;

    public RoomCartResponseDTO(Cart cart) {
        this.cart_id = cart.getId();
        List<RoomCartInfoDTO> roomCartInfoDTOList = new ArrayList<>();
        for(RoomCart roomCart : cart.getRoomCartList()){
            RoomCartInfoDTO roomCartInfoDTO = new RoomCartInfoDTO(roomCart);
            roomCartInfoDTOList.add(roomCartInfoDTO);
        }
        System.out.println(roomCartInfoDTOList.size()); // 정확히 나옴
        this.roomCartList = roomCartInfoDTOList;
    }
}
```

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartInfoDTO {

    private long room_id;
    private long cart_id;

    @Builder
    public RoomCartInfoDTO(long room_id, long cart_id) {
        this.room_id = room_id;
        this.cart_id = cart_id;
    }

    public RoomCartInfoDTO(RoomCart roomCart) {
        this.room_id = roomCart.getRoom().getId();
        this.cart_id = roomCart.getCart().getId();
    }
}
```

- JsonAutoDetect 설정 제거
    
    `@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)`
    
    - private 필드에 접근 가능하여 json으로 변환 가능하다.
- Fetch.Type을 EAGER로 바꾸는 것은 보안의 문제가 있으므로 고려하지 않았습니다.
- 또한, Entity Class에 @JsonProperty 또는 @JsonAutoDetect를 직접 선언할 수 있으나, Entity를 최대한 변경하지 않고자 DTO에 선언했습니다.
<br>

### 3. JPAQueryFactory 전역 설정과 DataJpaTest

**3 - 1. 원인**

```java
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.querydsl.jpa.impl.JPAQueryFactory' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:1824)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1383)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1337)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:910)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:788)
	... 108 more
```

해당 설정은 전역적으로 빈을 컨테이너에 생성하는 것이기 때문에 `Entity`와 `Respository` 빈만 생성하는 `@DataJpaTest의` 경우에는 `JpaQueryFactory` 빈을 생성하지 못하는 문제가 생기게 됩니다.
<br>

**3 - 2. 해결**

```java
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.aroom")
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
```

해당 문제를 해결하기 위해서는 실제 `JPAQueryFactory`를 사용하는 곳에서만 해당 빈을 생성하면 됩니다.
</details>

## 🤖 개인 역량 회고
