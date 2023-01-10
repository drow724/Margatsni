package com.instagl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.instagl.entity.Account;
import com.instagl.entity.Content;
import com.instagl.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServie {

	private final ContentRepository contentRepository;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Content save(Content content) {
		return contentRepository.save(content);
	}

	public List<Content> findByAccountId(Account account) {
		return contentRepository.findByAccount(account);
	}
}
