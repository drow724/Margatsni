package com.instagl.dto;

import com.instagl.entity.Location;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationDTO {

	private String address;

	private String name;

	private Double lat;

	private Double lng;

	public LocationDTO(Location location) {
		this.address = location.getAddress();
		this.name = location.getName();
		this.lat = location.getLat();
		this.lng = location.getLng();
	}
}