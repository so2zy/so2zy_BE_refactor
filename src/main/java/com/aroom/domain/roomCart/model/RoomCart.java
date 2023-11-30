package com.aroom.domain.roomCart.model;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.global.basetime.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomCart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_cart_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_product_id")
    private RoomProduct roomProduct;

    @Column(nullable = false)
    private int personnel;

    @Builder
    public RoomCart(Long id, Cart cart, RoomProduct roomProduct, int personnel) {
        this.id = id;
        this.cart = cart;
        this.roomProduct = roomProduct;
        this.personnel = personnel;
    }
}
