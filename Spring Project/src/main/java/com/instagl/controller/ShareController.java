package com.instagl.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.instagl.entity.Location;
import com.instagl.service.ShareService;
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

import com.instagl.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ShareController {
	
	private final AccountService accountService;
	
	private final ShareService shareService;
	
	@CrossOrigin
	@GetMapping("{accessToken}")
	public List<Location> getFeed(@PathVariable String accessToken) throws InterruptedException, ParseException {

		List<Location> contents = shareService.getLocationInfo(accessToken);
		return contents;
	}
}
