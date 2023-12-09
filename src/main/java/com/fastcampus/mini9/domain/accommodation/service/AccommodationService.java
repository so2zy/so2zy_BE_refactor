package com.fastcampus.mini9.domain.accommodation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.repository.AccommodationRepository;
import com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery;
import com.fastcampus.mini9.domain.accommodation.service.util.AccommodationServiceMapper;
import com.fastcampus.mini9.domain.member.entity.Member;
import com.fastcampus.mini9.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService implements AccommodationQuery {
	private final AccommodationRepository accommodationRepository;
	private final MemberRepository memberRepository;
	private final AccommodationServiceMapper mapper;

	@Override
	public SearchAccommodationsResponse searchAccommodations(SearchAccommodationsRequest searchAccommodationsRequest,
		UserPrincipal userPrincipal) {
		String region = searchAccommodationsRequest.region();
		String district = searchAccommodationsRequest.district();
		LocalDate startDate = searchAccommodationsRequest.startDate() != null ?
			searchAccommodationsRequest.startDate() : null;
		LocalDate endDate = searchAccommodationsRequest.endDate() != null ?
			searchAccommodationsRequest.endDate() : null;
		String category = searchAccommodationsRequest.category();
		String keyword = searchAccommodationsRequest.keyword();
		Integer pageNum = searchAccommodationsRequest.pageNum();
		Integer pageSize = searchAccommodationsRequest.pageSize();

		// TODO: 검색 쿼리(QueryDSL) 구현.
		//  1.해당 지역에 /
		//  2.해당 날짜에 재고가 있는 숙소 중 /
		//  3.해당 카테고리, 키워드에 해당하는 값들 /
		//  4. details정보까지 fetchJoin해서 /
		//  5.페이지네이션
		Page<Accommodation> all = accommodationRepository.searchByConditions(
			region, district,
			startDate, endDate,
			category, keyword,
			PageRequest.of(pageNum - 1, pageSize)
		);

		// TODO: 현재 로그인 상태면 검색된 accommodation에 좋아요 표시. 1안:UserPrincipal->Member. 2안:MemberService에 구현. 3안: 여기에 구현
		if (userPrincipal.id() != 0L) {
			Member loginMember = memberRepository.findById(userPrincipal.id()).orElseThrow();
			Page<SearchAccommodation> result = all
				.map(Accommodation -> SearchAccommodation.fromEntity(Accommodation, loginMember));
			return new SearchAccommodationsResponse(result.toList(), result.getNumber(), result.getSize(),
				result.getTotalPages(), result.getTotalElements(), result.isFirst(), result.isLast());
		}

		List<SearchAccommodation> accommodationResponses = mapper.entityListToResponseList(all.toList());
		return new SearchAccommodationsResponse(accommodationResponses,
			searchAccommodationsRequest.pageNum(), searchAccommodationsRequest.pageSize(),
			all.getTotalPages(), all.getTotalElements(), all.isFirst(), all.isLast());
	}

	@Override
	public FindAccommodationResponse findAccommodation(
		FindAccommodationRequest findAccommodationRequest) {
		Accommodation findAccommodation = accommodationRepository.findById(findAccommodationRequest.id())
			.orElseThrow();
		FindAccommodationResponse findAccommodationResponse = mapper.entityToResponseDetail(
			findAccommodation);
		return findAccommodationResponse;
	}
}
