package com.instagl.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.instagl.dto.ContentDTO;
import com.instagl.dto.UserDTO;
import com.instagl.entity.Account;
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

	private final ExecutorService executor = Executors.newFixedThreadPool(1);

	@CrossOrigin
	@GetMapping
	public List<ContentDTO> getFeed(@RequestParam String accessToken) throws InterruptedException, ParseException {
        return shareService.getLocationInfo(accessToken);
	}

	@CrossOrigin
	@PatchMapping
	public UserDTO updateFeed(@RequestBody UserDTO userDTO) {
		if(userDTO.getUpdating()) {
			throw new IllegalStateException("업데이트 중입니다.");
		}

		Account account = accountService.changeUpdateState(userDTO.getFeedId());

		executor.execute(() -> shareService.updateFeed(account, userDTO.getAccessToken()));

		return new UserDTO(account.getFeedId(), userDTO.getAccessToken(), account);
	}

	@CrossOrigin
	@PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@RequestBody String url){

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(URLDecoder.decode(url, StandardCharsets.UTF_8), byte[].class);
	}
}
