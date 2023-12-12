package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.QAccommodationListResponse_InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.exception.InvalidOrderByException;
import com.aroom.domain.accommodation.model.QAccommodation;
import com.aroom.domain.accommodation.model.QAccommodationImage;
import com.aroom.domain.room.model.QRoom;
import com.aroom.domain.roomProduct.model.QRoomProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAccommodation accommodation = QAccommodation.accommodation;
    private final QAccommodationImage accommodationImage = QAccommodationImage.accommodationImage;
    private final QRoom room = QRoom.room;
    private final QRoomProduct roomProduct = QRoomProduct.roomProduct;

    public AccommodationRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<AccommodationListResponse.InnerClass> getAll(Pageable pageable) {
        log.info("select all commands selected");

        List<AccommodationListResponse.InnerClass> content = jpaQueryFactory
            .select(new QAccommodationListResponse_InnerClass(
                accommodation.id,
                accommodation.name,
                accommodation.latitude,
                accommodation.longitude,
                accommodation.address,
                accommodation.likeCount,
                accommodation.phoneNumber,
                accommodationImage.url.as("accommodationImageUrl"),
                room.price.min()
            ))
            .from(accommodation)
            .join(accommodation.roomList, room)
            .leftJoin(accommodation.accommodationImageList, accommodationImage)
            .groupBy(accommodation.id)
            .offset(pageable.getPageNumber())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = jpaQueryFactory
            .select(accommodation.count())
            .from(accommodation)
            .fetchOne();

        log.info("count query value is :{}", count);

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<AccommodationListResponse.InnerClass> searchByCondition(
        SearchCondition searchCondition,
        Pageable pageable) {

        log.info("searchByCondition commands selected");

        List<AccommodationListResponse.InnerClass> content = jpaQueryFactory
            .select(new QAccommodationListResponse_InnerClass(
                accommodation.id,
                accommodation.name,
                accommodation.latitude,
                accommodation.longitude,
                accommodation.address,
                accommodation.likeCount,
                accommodation.phoneNumber,
                accommodationImage.url.as("accommodationImageUrl"),
                searchCondition.getOrderBy().equals("asc") ? room.price.min() : room.price.max())
            )
            .from(accommodation)
            .join(accommodation.roomList, room)
            .leftJoin(accommodation.accommodationImageList, accommodationImage)
            .where(booleanBuilderProvider(searchCondition))
            .groupBy(accommodation.id)
            .offset(pageable.getPageNumber())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = jpaQueryFactory
            .select(accommodation.count())
            .from(accommodation)
            .join(accommodation.roomList, room)
            .leftJoin(accommodation.accommodationImageList, accommodationImage)
            .where(booleanBuilderProvider(searchCondition))
            .fetchOne();

        log.info("count query value is :{}", count);
        log.info("orderBy is {}", searchCondition.getOrderBy());
        return new PageImpl<>(content, pageable, count);

    }


    @Override
    public Page<AccommodationListResponse.InnerClass> searchByConditionWithSort(
        SearchCondition searchCondition,
        Pageable pageable) {
        log.info("searchByConditionWithSort commands selected");

        List<AccommodationListResponse.InnerClass> content = jpaQueryFactory
            .select(new QAccommodationListResponse_InnerClass(
                accommodation.id,
                accommodation.name,
                accommodation.latitude,
                accommodation.longitude,
                accommodation.address,
                accommodation.likeCount,
                accommodation.phoneNumber,
                accommodationImage.url.as("accommodationImageUrl"),
                searchCondition.getOrderBy().equals("asc") ? room.price.min() : room.price.max())
            )
            .from(accommodation)
            .join(accommodation.roomList, room)
            .leftJoin(accommodation.accommodationImageList, accommodationImage)
            .where(booleanBuilderProvider(searchCondition))
            .groupBy(accommodation.id)
            .offset(pageable.getPageNumber())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifierProvider(searchCondition))
            .fetch();

        Long count = jpaQueryFactory
            .select(accommodation.count())
            .from(accommodation)
            .join(accommodation.roomList, room)
            .leftJoin(accommodation.accommodationImageList, accommodationImage)
            .where(booleanBuilderProvider(searchCondition))
            .fetchOne();

        log.info("count query value is :{}", count);
        log.info("orderBy is {}", searchCondition.getOrderBy());
        return new PageImpl<>(content, pageable, count);
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
        if (searchCondition.getLowestPrice() != null){
            booleanBuilder.and(
                room.price.goe(Integer.parseInt(searchCondition.getLowestPrice())));
        }
        if (searchCondition.getHighestPrice() != null){
            booleanBuilder.and(
                room.price.loe(Integer.parseInt(searchCondition.getHighestPrice())));
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
    private OrderSpecifier<?> orderSpecifierProvider(SearchCondition searchCondition) {
        if (searchCondition.getOrderCondition() != null) {
            //Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            Order direction = getOrderBy(searchCondition);
            switch (searchCondition.getOrderCondition()) {
                case "" -> {
                    return new OrderSpecifier<>(direction, accommodation.id);
                }
                case "name" -> {
                    return new OrderSpecifier<>(direction, accommodation.name);
                }
                case "likeCount" -> {
                    return new OrderSpecifier<>(direction, accommodation.likeCount);
                }
                case "price" -> {
                    if (direction == Order.ASC) {
                        return new OrderSpecifier<>(direction, room.price.min());
                    }
                    return new OrderSpecifier<>(direction, room.price.max());
                }
                case "checkIn" -> {
                    return new OrderSpecifier<>(direction, accommodation.roomList.any().checkIn);
                }
                case "capacity" -> {
                    return new OrderSpecifier<>(direction,
                        accommodation.roomList.any().maxCapacity);
                }
                case "soldCount" -> {
                    return new OrderSpecifier<>(direction, accommodation.roomList.any().soldCount);
                }
            }
        }
        return null;
    }
    public static Order getOrderBy(SearchCondition searchCondition) {
        Order direction;
        if (searchCondition.getOrderBy().equalsIgnoreCase("ASC")) {
            direction = Order.ASC;
        } else if (searchCondition.getOrderBy().equalsIgnoreCase("DESC")) {
            direction = Order.DESC;
        } else {
            throw new InvalidOrderByException();
        }
        return direction;
    }
}
