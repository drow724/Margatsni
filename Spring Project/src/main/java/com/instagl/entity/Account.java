package com.instagl.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.instagl.MemberGrade;
import com.instagl.converter.BooleanToBinaryConverter;
import jakarta.persistence.*;
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

	private String feedId;

	private String profileImgPath;

	private MemberGrade memberGrade = MemberGrade.NORMAL;

	@Convert(converter = BooleanToBinaryConverter.class)
	private Boolean updating;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<Content> contents = new ArrayList<>();
	
	public Account(String username, String profilePicUrl, String biography, Long follow, Long followed, String feedId, String profileImgPath, Boolean updating) {
		this.username = username;
		this.profilePicUrl = profilePicUrl;
		this.biography = biography;
		this.follow = follow;
		this.followed = followed;
		this.feedId = feedId;
		this.profileImgPath = profileImgPath;
		this.updating = updating;
	}

	public void changeUpdating() {
		this.updating = Boolean.TRUE;
	}
}
