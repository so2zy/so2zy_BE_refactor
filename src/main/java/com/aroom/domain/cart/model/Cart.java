package com.aroom.domain.cart.model;

import com.aroom.domain.member.model.Member;
import com.aroom.global.basetime.BaseTimeEntity;
import com.aroom.domain.roomCart.model.RoomCart;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RoomCart> roomCartList = new ArrayList<>();

    public void postRoomCarts(RoomCart roomCart) {
        roomCartList.add(roomCart);
    }

    @Builder
    public Cart(Long id, Member member, List<RoomCart> roomCartList) {
        this.id = id;
        this.member = member;
        this.roomCartList = roomCartList;
    }

    @Builder
    public Cart(Member member, List<RoomCart> roomCartList) {
        this.member = member;
        this.roomCartList = roomCartList;
    }

    @Builder
    public Cart(Member member) {
        this.member = member;
    }

    public void clearCart(){
        this.roomCartList.clear();
    }
}
