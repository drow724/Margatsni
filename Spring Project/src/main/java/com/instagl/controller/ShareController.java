package com.instagl.controller;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.parser.ParseException;
import org.springframework.boot.ApplicationArguments;
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
import com.instagl.service.ShareService;
import com.microsoft.playwright.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ShareController {
	
	private final ShareService shareService;
	
	private final AccountService accountService;
	
	private final ContentServie contentServie;
	
	@CrossOrigin
	@GetMapping("{username}")
	public List<Content> getFeed(@PathVariable String username) throws InterruptedException, ParseException {
		
		List<Content> contents = new ArrayList<>();
		
		Optional<Account> option = accountService.getAccount(username);
		
		if(option.isEmpty()) {
			contents = shareService.getAccount(username);
		} else {
			contents = contentServie.findByAccountId(option.get());
		}
		
		return contents;
	}
	
	@CrossOrigin
	@PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@RequestBody String url) throws InterruptedException, ParseException {
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(URLDecoder.decode(url, StandardCharsets.UTF_8), byte[].class);
	}
}
