package com.fastcampus.mini9.domain.accommodation.vo;

import com.fastcampus.mini9.common.exception.ErrorCode;
import com.fastcampus.mini9.domain.accommodation.exception.NoSuchAccommodationType;
import lombok.Getter;

@Getter
public enum AccommodationType {
	HOTEL("호텔", "HOTEL"), MOTEL("모텔", "MOTEL"), PENSION("펜션", "PENSION"), RESORT("리조트", "RESORT"), NOT_CLASSIFIED("없음", "NOT_CLASSIFIED");

	private final String korName;
	private final String engName;

	AccommodationType(String korName, String engName) {
		this.korName = korName;
		this.engName = engName;
	}

	public static AccommodationType getTypeKor(String korName) throws NoSuchAccommodationType {
		for (AccommodationType type : AccommodationType.values()) {
			if(type.getKorName().equals(korName)) {
				return type;
			}
		}
		throw new NoSuchAccommodationType(ErrorCode.NoSuchElement);
	}

	public static AccommodationType getTypeEng(String engName) throws NoSuchAccommodationType {
		for (AccommodationType type : AccommodationType.values()) {
			if(type.getEngName().equals(engName)) {
				return type;
			}
		}
		throw new NoSuchAccommodationType(ErrorCode.NoSuchElement);
	}

	@Override
	public String toString() {
		return engName;
	}
}
