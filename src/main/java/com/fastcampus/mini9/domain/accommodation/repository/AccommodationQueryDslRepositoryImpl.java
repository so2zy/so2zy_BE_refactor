package com.fastcampus.mini9.domain.accommodation.repository;

import static com.fastcampus.mini9.domain.accommodation.entity.accommodation.QAccommodation.*;
import static com.fastcampus.mini9.domain.accommodation.entity.room.QStock.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.exception.NoSuchAccommodationType;
import com.fastcampus.mini9.domain.accommodation.vo.AccommodationType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class AccommodationQueryDslRepositoryImpl implements AccommodationQueryDslRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public AccommodationQueryDslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	// TODO: stock 없는 데이터 필터링
	@Override
	public Page<Accommodation> searchByConditions(
		String regionReq, String districtReq,
		LocalDate startDateReq, LocalDate endDateReq,
		String categoryReq, String keywordReq,
		Pageable pageReq
	) throws NoSuchAccommodationType {
		List<Accommodation> content = jpaQueryFactory
			.select(accommodation)
			.from(accommodation)
			// .join(stock)
			// .on(
			// 	stock.room.in(accommodation.rooms),
			// 	dateBetween(startDateReq, endDateReq),
			// 	stock.quantity.gt(0)
			// )
			.where(
				locationEq(regionReq, districtReq),
				categoryEq(categoryReq),
				keywordEq(keywordReq)
			)
			.offset(pageReq.getOffset())
			.limit(pageReq.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(accommodation.count())
			.from(accommodation)
			// .join(stock)
			// .on(
			// 	stock.room.in(accommodation.rooms),
			// 	dateBetween(startDateReq, endDateReq),
			// 	stock.quantity.gt(0)
			// )
			.where(
				locationEq(regionReq, districtReq),
				categoryEq(categoryReq),
				keywordEq(keywordReq)
			);

		return PageableExecutionUtils.getPage(content, pageReq, countQuery::fetchOne);
	}

	private BooleanExpression dateBetween(LocalDate startDateReq, LocalDate endDateReq) {
		if (startDateReq != null && endDateReq != null) {
			return stock.date.between(startDateReq, endDateReq);
		}
		return stock.date.after(LocalDate.now());
	}

	private BooleanExpression keywordEq(String keywordReq) {
		return keywordReq != null ? accommodation.name.contains(keywordReq) : null;
	}

	private BooleanExpression categoryEq(String categoryReq) throws NoSuchAccommodationType {
		return categoryReq != null ? accommodation.type.eq(AccommodationType.getTypeKor(categoryReq)) : null;
	}

	private BooleanExpression locationEq(final String regionReq, final String districtReq) {
		if (regionReq != null && districtReq != null) {
			return accommodation.location.region.name.eq(regionReq)
				.and(accommodation.location.district.name.eq(districtReq));
		}
		if (regionReq != null) {
			return accommodation.location.region.name.eq(regionReq);
		}
		if (districtReq != null) {
			return accommodation.location.district.name.eq(districtReq);
		}
		return null;
	}
}
