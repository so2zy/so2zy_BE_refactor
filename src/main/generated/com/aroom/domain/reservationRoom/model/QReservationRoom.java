package com.aroom.domain.reservationRoom.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationRoom is a Querydsl query type for ReservationRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationRoom extends EntityPathBase<ReservationRoom> {

    private static final long serialVersionUID = -2110914096L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationRoom reservationRoom = new QReservationRoom("reservationRoom");

    public final com.aroom.global.basetime.QBaseTimeEntity _super = new com.aroom.global.basetime.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> personnel = createNumber("personnel", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.aroom.domain.reservation.model.QReservation reservation;

    public final com.aroom.domain.room.model.QRoom room;

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReservationRoom(String variable) {
        this(ReservationRoom.class, forVariable(variable), INITS);
    }

    public QReservationRoom(Path<? extends ReservationRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationRoom(PathMetadata metadata, PathInits inits) {
        this(ReservationRoom.class, metadata, inits);
    }

    public QReservationRoom(Class<? extends ReservationRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new com.aroom.domain.reservation.model.QReservation(forProperty("reservation"), inits.get("reservation")) : null;
        this.room = inits.isInitialized("room") ? new com.aroom.domain.room.model.QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

