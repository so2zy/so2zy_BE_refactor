package com.fastcampus.mini9.domain.accommodation.entity.location;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Embeddable
@Getter
public class Location {

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;

	public void setRegion(Region region) {
		this.region = region;
	}
}
