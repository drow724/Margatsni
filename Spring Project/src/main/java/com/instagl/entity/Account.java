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
	
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Content> contents = new ArrayList<>();
	
	public Account(String username, String profilePicUrl, String biography, Long follow, Long followed) {
		this.username = username;
		this.profilePicUrl = profilePicUrl;
		this.biography = biography;
		this.follow = follow;
		this.followed = followed;
	}
	
}
