package com.instagl.dto;

import com.instagl.entity.Content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentDTO {

	private String caption;

	private String address;

	private String city;

	private String name;

	private Double lat;

	private Double lng;

	public ContentDTO(Content content) {
		this.caption = content.getCaption();
		this.address = content.getLocation().getAddress();
		this.city = content.getLocation().getCity();
		this.name = content.getLocation().getName();
		this.lat = content.getLocation().getLat();
		this.lng = content.getLocation().getLng();
	}
}
