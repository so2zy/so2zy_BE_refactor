package com.aroom.domain.accommodation.repository;

import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.QAccommodation;
import com.aroom.domain.room.model.QRoom;
import com.aroom.domain.roomProduct.model.QRoomProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAccommodation accommodation = QAccommodation.accommodation;
    private final QRoom room = QRoom.room;
    private final QRoomProduct roomProduct = QRoomProduct.roomProduct;

    public AccommodationRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Accommodation> getAll(Pageable pageable) {
        return jpaQueryFactory.selectFrom(accommodation)
            .from(accommodation)
            .offset((long) pageable.getPageNumber() * pageable.getPageSize() + 1)
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public List<Accommodation> getAccommodationBySearchCondition(
        SearchCondition searchCondition,
        Pageable pageable) {

        return jpaQueryFactory.select(accommodation)
            .from(accommodation)
            .where(booleanBuilderProvider(searchCondition),
                greaterOrEqualsCapacity(searchCondition))
            .offset((long) pageable.getPageNumber() * pageable.getPageSize() + 1)
            .limit(pageable.getPageSize())
            .distinct()
            .fetch();

    }

    //날짜 필터링은 JOIN을 해야하기 때문에 메서드 분리
    @Override
    public List<Accommodation> getAccommodationByDateSearchCondition(
        SearchCondition searchCondition,
        Pageable pageable) {

        return jpaQueryFactory
            .select(accommodation)
            .from(accommodation)
            .join(accommodation.roomList, room)
            .join(room.roomProductList, roomProduct)
            .where(betweenDate(searchCondition),
                greaterThanStock(0)
                , greaterOrEqualsCapacity(searchCondition))
            .offset((long) pageable.getPageNumber() * pageable.getPageSize())
            .limit(pageable.getPageSize())
            .distinct()
            .fetch();

    }

    @Override
    public List<Accommodation> getAccommodationByDateSearchConditionWithSortCondition(
        SearchCondition searchCondition,
        Pageable pageable,
        Sort sortCondition) {
        return jpaQueryFactory
            .select(accommodation)
            .from(accommodation)
            .join(accommodation.roomList, room)
            .where(booleanBuilderForDate(searchCondition)
                , booleanBuilderProvider(searchCondition)
                , greaterOrEqualsCapacity(searchCondition))
            .offset((long) pageable.getPageNumber() * pageable.getPageSize())
            .limit(pageable.getPageSize())
            .orderBy(getOrderBy(sortCondition))
            .distinct()
            .fetch();
    }


    @Override
    public List<Accommodation> getAccommodationBySearchConditionWithSortCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {
        return jpaQueryFactory
            .select(accommodation)
            .from(accommodation)
            .join(accommodation.roomList, room)
            .where(booleanBuilderProvider(searchCondition)
                , greaterOrEqualsCapacity(searchCondition))
            .offset(((long) pageable.getPageNumber() * pageable.getPageSize()))
            .limit(pageable.getPageSize())
            .orderBy(getOrderBy(sortCondition))
            .distinct()
            .fetch();
    }

    private BooleanBuilder booleanBuilderProvider(SearchCondition searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (searchCondition.getAreaName() != null) {
            booleanBuilder.and(
                accommodation.addressEntity.areaName.eq(searchCondition.getAreaName()));
        }
        if (searchCondition.getSigunguName() != null) {
            booleanBuilder.and(
                accommodation.addressEntity.sigunguName.eq(searchCondition.getSigunguName()));
        }
        if (searchCondition.getName() != null) {
            booleanBuilder.and(
                accommodation.name.contains(searchCondition.getName()));
        }
        if (searchCondition.getLikeCount() != null) {
            booleanBuilder.and(
                accommodation.likeCount.goe(Integer.parseInt(searchCondition.getLikeCount())));
        }
        if (searchCondition.getPhoneNumber() != null) {
            booleanBuilder.and(
                accommodation.phoneNumber.eq(searchCondition.getPhoneNumber()));
        }
        //lowestPrice와 higestPrice가 둘 다 NULL이 아닌 경우
        if (searchCondition.getLowestPrice() != null &&
            searchCondition.getHighestPrice() != null) {
            booleanBuilder.and(accommodation.roomList.any().price.between(
                Integer.parseInt(searchCondition.getLowestPrice())
                , Integer.parseInt(searchCondition.getHighestPrice()))
            );
        } else {
            // lowestPrice는 존재하고, highestPrice만 NULL인 경우
            if (searchCondition.getLowestPrice() != null &&
                searchCondition.getHighestPrice() == null) {
                booleanBuilder.and(accommodation.roomList.any().price.goe(
                    Integer.parseInt(searchCondition.getLowestPrice())));
            }
            // lowestPrice는 NULL이고 , highestPrice만 존재하는 경우
            if (searchCondition.getLowestPrice() == null &&
                searchCondition.getHighestPrice() != null) {
                booleanBuilder.and(accommodation.roomList.any().price.loe(
                    Integer.parseInt(searchCondition.getHighestPrice())));
            }
        }
        if (searchCondition.getCheckIn() != null) {
            booleanBuilder.and(
                accommodation.roomList.any().checkIn.after(searchCondition.getCheckIn()));
        }
        if (searchCondition.getCheckOut() != null) {
            booleanBuilder.and(
                accommodation.roomList.any().checkOut.before(searchCondition.getCheckOut()));
        }

        return booleanBuilder;
    }

    private BooleanExpression greaterOrEqualsCapacity(SearchCondition searchCondition) {
        if (searchCondition.getCapacity() == null) {
            return null;
        }
        return accommodation.roomList.any().maxCapacity.goe(searchCondition.getCapacity());
    }

    private BooleanBuilder booleanBuilderForDate(SearchCondition searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        //        날짜 필터링
        if (searchCondition.getStartDate() != null && searchCondition.getEndDate() != null) {
            booleanBuilder.and(
                    room.roomProductList.any().startDate.between(searchCondition.getStartDate(),
                        searchCondition.getEndDate()))
                .and(room.roomProductList.any().stock.gt(0));
        }

        return booleanBuilder;
    }

    private BooleanExpression betweenDate(SearchCondition searchCondition) {
        if (searchCondition.getStartDate() == null || searchCondition.getEndDate() == null) {
            return null;
        }
        return roomProduct.startDate.between(searchCondition.getStartDate(),
            searchCondition.getEndDate());
    }

    private BooleanExpression greaterThanStock(Integer stock) {
        if (stock == null) {
            return null;
        }
        return roomProduct.stock.gt(stock);
    }


    private OrderSpecifier<?> getOrderBy(Sort sortCondition) {
        if (!sortCondition.isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : sortCondition) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case NO_ORDER_CONDITION:
                        return new OrderSpecifier(direction, accommodation.id);
                    case "name":
                        return new OrderSpecifier(direction, accommodation.name);
                    case "likeCount":
                        return new OrderSpecifier(direction, accommodation.likeCount);
                    case "price":
                        return new OrderSpecifier(direction, accommodation.roomList.any().price);
                    case "checkIn":
                        return new OrderSpecifier(direction, accommodation.roomList.any().checkIn);
                    case "capacity":
                        return new OrderSpecifier(direction,
                            accommodation.roomList.any().maxCapacity);
                    case "soldCount":
                        return new OrderSpecifier(direction,
                            accommodation.roomList.any().soldCount);
                }
            }
        }

        return null;
    }
}
