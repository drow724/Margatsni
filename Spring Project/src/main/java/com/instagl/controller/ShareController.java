package com.instagl.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.instagl.entity.Account;
import com.instagl.entity.Content;
import com.instagl.service.AccountService;
import com.instagl.service.ContentServie;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ShareController {
	
	private final AccountService accountService;
	
	private final ContentServie contentServie;
	
	@CrossOrigin
	@GetMapping("{username}")
	public List<Content> getFeed(@PathVariable String username) throws InterruptedException, ParseException {
		
		Account account = accountService.getAccount(username).orElseThrow(() -> new NoSuchElementException("계정이 존재하지 않습니다."));
		
		List<Content> contents = contentServie.findByAccountId(account);
		return contents;
	}
	
	@CrossOrigin
	@PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@RequestBody String url) throws InterruptedException, ParseException {
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(URLDecoder.decode(url, StandardCharsets.UTF_8), byte[].class);
	}
}
