package com.instagl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagl.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
