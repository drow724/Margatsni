package com.instagl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

	@Id
	private Long id;

	private String address;
	
	private String name;
	
	private Double lat;
	
	private Double lng;

	@JsonIgnore
	@OneToMany(mappedBy = "location")
	private List<Content> contents = new ArrayList<>();

	public Location(Long id, String address, String name, Double lat, Double lng) {
		this.id = id;
		this.address = address;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
}