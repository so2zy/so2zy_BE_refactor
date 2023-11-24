package com.aroom.domain.accommodation.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccommodationImage is a Querydsl query type for AccommodationImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationImage extends EntityPathBase<AccommodationImage> {

    private static final long serialVersionUID = -572915691L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccommodationImage accommodationImage = new QAccommodationImage("accommodationImage");

    public final com.aroom.global.basetime.QBaseTimeEntity _super = new com.aroom.global.basetime.QBaseTimeEntity(this);

    public final QAccommodation accommodation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QAccommodationImage(String variable) {
        this(AccommodationImage.class, forVariable(variable), INITS);
    }

    public QAccommodationImage(Path<? extends AccommodationImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccommodationImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccommodationImage(PathMetadata metadata, PathInits inits) {
        this(AccommodationImage.class, metadata, inits);
    }

    public QAccommodationImage(Class<? extends AccommodationImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accommodation = inits.isInitialized("accommodation") ? new QAccommodation(forProperty("accommodation")) : null;
    }

}

