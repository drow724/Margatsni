package com.instagl.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String profilePicUrl;

	private String biography;
	
	private Long follow;
	
	private Long followed;

	private String access_token;

	private String feed_id;

	private String profile_img_path;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Content> contents = new ArrayList<>();
	
	public Account(String username, String profilePicUrl, String biography, Long follow, Long followed, String access_token, String feed_id, String profile_img_path) {
		this.username = username;
		this.profilePicUrl = profilePicUrl;
		this.biography = biography;
		this.follow = follow;
		this.followed = followed;
		this.access_token = access_token;
		this.feed_id = feed_id;
		this.profile_img_path = profile_img_path;
	}
	
}
