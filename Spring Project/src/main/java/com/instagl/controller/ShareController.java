package com.instagl.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.instagl.dto.ContentDTO;
import com.instagl.entity.Content;
import com.instagl.entity.Location;
import com.instagl.service.ShareService;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
	@GetMapping
	public List<ContentDTO> getFeed(@RequestParam String accessToken) throws InterruptedException, ParseException {
		List<ContentDTO> contents = shareService.getLocationInfo(accessToken);
		return contents;
	}

	@CrossOrigin
	@PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@RequestBody String url){

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(URLDecoder.decode(url, StandardCharsets.UTF_8), byte[].class);
	}
}
