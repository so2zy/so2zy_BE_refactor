package com.aroom.domain.roomCart.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomCart is a Querydsl query type for RoomCart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoomCart extends EntityPathBase<RoomCart> {

    private static final long serialVersionUID = -898563032L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomCart roomCart = new QRoomCart("roomCart");

    public final com.aroom.global.basetime.QBaseTimeEntity _super = new com.aroom.global.basetime.QBaseTimeEntity(this);

    public final com.aroom.domain.cart.model.QCart cart;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.aroom.domain.room.model.QRoom room;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRoomCart(String variable) {
        this(RoomCart.class, forVariable(variable), INITS);
    }

    public QRoomCart(Path<? extends RoomCart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomCart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomCart(PathMetadata metadata, PathInits inits) {
        this(RoomCart.class, metadata, inits);
    }

    public QRoomCart(Class<? extends RoomCart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new com.aroom.domain.cart.model.QCart(forProperty("cart"), inits.get("cart")) : null;
        this.room = inits.isInitialized("room") ? new com.aroom.domain.room.model.QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

