package com.fastcampus.mini9.domain.accommodation.entity.accommodation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccommodationDetails {
	@Id
	@Column(name = "accommodation_id")
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "accommodation_id")
	private Accommodation accommodation;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;
	private String address;
	private String latitude;
	private String longitude;
	private String tel;
	private boolean parking;
	private boolean cooking;
	private String others;
}
