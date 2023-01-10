package com.instagl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.instagl.entity.Image;
import com.instagl.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveAll(List<Image> images) {
		imageRepository.saveAll(images);
	}
}
