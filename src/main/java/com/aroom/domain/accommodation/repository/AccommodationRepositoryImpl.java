package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.QAccommodation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QAccommodation accommodation = QAccommodation.accommodation;

    @Override
    public List<Accommodation> getAccommodationBySearchCondition(SearchCondition searchCondition,
        Pageable pageable) {

        return jpaQueryFactory.select(accommodation)
            .from(accommodation)
            .where(booleanBuilderProvider(searchCondition))
            .offset(pageable.getPageNumber())
            .limit(pageable.getPageSize())
            .orderBy(getOrderBy(pageable))
            .fetch();

    }

    private BooleanBuilder booleanBuilderProvider(SearchCondition searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (searchCondition.getName() != null) {
            booleanBuilder.and(accommodation.name.contains(searchCondition.getName()));
        }
        if (searchCondition.getAddressCode() != null) {
            booleanBuilder.and(
                accommodation.addressCode.contains(searchCondition.getAddressCode()));
        }
        if (searchCondition.getLikeCount() != null) {
            booleanBuilder.and(
                accommodation.likeCount.goe(Integer.parseInt(searchCondition.getLikeCount())));
        }
        if (searchCondition.getPhoneNumber() != null) {
            booleanBuilder.and(accommodation.phoneNumber.eq(searchCondition.getPhoneNumber()));
        }
        if (searchCondition.getLowestPrice() != null) {
            booleanBuilder.and(accommodation.roomList.any().price.goe(
                Integer.parseInt(searchCondition.getLowestPrice())));
        }
        if (searchCondition.getHighestPrice() != null) {
            booleanBuilder.and(accommodation.roomList.any().price.goe(
                Integer.parseInt(searchCondition.getHighestPrice())));
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


    private OrderSpecifier<?> getOrderBy(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : pageable.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
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
                }
            }
        }
        return null;
    }
}
