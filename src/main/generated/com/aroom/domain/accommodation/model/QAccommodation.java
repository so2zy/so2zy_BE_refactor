package com.aroom.domain.accommodation.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccommodation is a Querydsl query type for Accommodation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodation extends EntityPathBase<Accommodation> {

    private static final long serialVersionUID = -1364240154L;

    public static final QAccommodation accommodation = new QAccommodation("accommodation");

    public final com.aroom.global.basetime.QBaseTimeEntity _super = new com.aroom.global.basetime.QBaseTimeEntity(this);

    public final ListPath<AccommodationImage, QAccommodationImage> accommodationImageList = this.<AccommodationImage, QAccommodationImage>createList("accommodationImageList", AccommodationImage.class, QAccommodationImage.class, PathInits.DIRECT2);

    public final StringPath addressCode = createString("addressCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Float> latitude = createNumber("latitude", Float.class);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final NumberPath<Float> longitude = createNumber("longitude", Float.class);

    public final StringPath name = createString("name");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.aroom.domain.room.model.Room, com.aroom.domain.room.model.QRoom> roomList = this.<com.aroom.domain.room.model.Room, com.aroom.domain.room.model.QRoom>createList("roomList", com.aroom.domain.room.model.Room.class, com.aroom.domain.room.model.QRoom.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodation(String variable) {
        super(Accommodation.class, forVariable(variable));
    }

    public QAccommodation(Path<? extends Accommodation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodation(PathMetadata metadata) {
        super(Accommodation.class, metadata);
    }

}

