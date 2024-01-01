package com.instagl.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.instagl.BooleanToBinaryConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Convert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

	@Id
	private Long id;
	
	private String username;
	
	private String profilePicUrl;

	private String biography;
	
	private Long follow;
	
	private Long followed;

	private String feed_id;

	private String profile_img_path;

	@Convert(converter = BooleanToBinaryConverter.class)
	private Boolean updating;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Content> contents = new ArrayList<>();
	
	public Account(Long id, String username, String profilePicUrl, String biography, Long follow, Long followed, String feed_id, String profile_img_path, Boolean updating) {
		this.id = id;
		this.username = username;
		this.profilePicUrl = profilePicUrl;
		this.biography = biography;
		this.follow = follow;
		this.followed = followed;
		this.feed_id = feed_id;
		this.profile_img_path = profile_img_path;
		this.updating = updating;
	}

	public void changeUpdating() {
		this.updating = Boolean.TRUE;
	}
}
