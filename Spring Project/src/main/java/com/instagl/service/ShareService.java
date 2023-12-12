package com.instagl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagl.dto.ContentDTO;
import com.instagl.entity.Image;
import com.instagl.repository.LocationRepository;
import com.microsoft.playwright.*;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.instagl.entity.Content;
import com.instagl.entity.Location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ShareService {

	@Value("${instagram.me.media}")
	private String userMediaUrl;

	@Value("${instagram.media}")
	private String mediaUrl;

	private ExecutorService executor = Executors.newFixedThreadPool(100);
	private final RestTemplate restTemplate = new RestTemplate();

	private final ObjectMapper mapper = new ObjectMapper();

	private final BrowserType.LaunchOptions launchOptions;

	private final AccountService accountService;

	private final ContentServie contentServie;

	private final ImageService imageService;

	private final LocationRepository locationRepository;

	//@Retryable(maxAttempts = 3)
	public List<ContentDTO> getLocationInfo(String accessToken) throws InterruptedException, ParseException {
		System.out.println("userMediaUrl + accessToken = " + userMediaUrl + accessToken);
		Map res = restTemplate.getForObject(userMediaUrl + accessToken, Map.class);

		List<Map<String, Object>> data = (List<Map<String, Object>>) res.get("data");

		List<CompletableFuture<Content>> dataList = data.stream().map(d -> CompletableFuture.supplyAsync(() -> {
			Content content = null;
			try (Playwright playwright = Playwright.create()) {

				Browser context = playwright.chromium().launch(launchOptions);

				Page page = context.newPage();

				page.navigate(String.valueOf(d.get("permalink")));

				Response postResponse = page
						.waitForResponse(response -> response.url().contains("https://www.instagram.com/api/graphql"), () -> {
						});

				Map<String, Object> post = mapper.readValue(postResponse.text(), Map.class);

				Map<String, Object> postData = (Map<String, Object>) post.get("data");
				Map<String, Object> xdt_shortcode_media = (Map<String, Object>) postData.get("xdt_shortcode_media");
				Map<String, Object> location = (Map<String, Object>) xdt_shortcode_media.get("location");

				if(location == null) {
					return content;
				}

				Optional<Location> optional = locationRepository.findById(Long.parseLong(location.get("id").toString()));
				if(optional.isPresent()) {
					Location loc = optional.get();
					return new Content(Long.parseLong(d.get("id").toString()), String.valueOf(d.get("caption")), loc);
				}

				page.navigate("https://www.instagram.com/explore/locations/" + location.get("id") + "/?img_index=1");

				Response locationResponse = page.waitForResponse(response -> response.url().contains("https://www.instagram.com/api/v1/locations/web_info/?"), () -> {});

				Map<String, Object> locationData = mapper.readValue(locationResponse.text(), Map.class);
				Map<String, Object> native_location_data = (Map<String, Object>) locationData.get("native_location_data");
				Map<String, Object> location_info = (Map<String, Object>) native_location_data.get("location_info");

				Location loc = new Location(Long.parseLong(location.get("id").toString()), String.valueOf(location_info.get("location_address")), String.valueOf(location_info.get("name")),
						Double.parseDouble(location_info.get("lat").toString()),
						Double.parseDouble(location_info.get("lng").toString()));

				locationRepository.save(loc);

				content = new Content(Long.parseLong(d.get("id").toString()), String.valueOf(d.get("caption")), loc);

			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}

			return content;
		}, executor)).collect(Collectors.toList());

		List<Content> contents = dataList.stream().map(output -> output.join()).filter(output -> output != null).collect(Collectors.toList());

		List<CompletableFuture<ContentDTO>> contentDTOs = contents.stream().map(content -> CompletableFuture.supplyAsync(() -> {
			Map<String, Object> response = restTemplate.getForObject(mediaUrl.replaceAll("\\{id\\}", content.getId().toString()), Map.class);
			if(response.get("children") == null) {
				List<Image> images = new ArrayList<>();
				images.add(new Image(String.valueOf(response.get("media_url"))));
				return new ContentDTO(content, images);
			}
			Map<String, Object> children = (Map<String, Object>) response.get("children");
			List<Map<String, Object>> map = (List<Map<String, Object>>) children.get("data");

			List<String> ids = map.stream().map(d -> String.valueOf(d.get("id"))).collect(Collectors.toList());

			List<CompletableFuture<Image>> imageFuture = ids.stream().map(id -> CompletableFuture.supplyAsync(() -> {
				Map<String, Object> child = restTemplate.getForObject(mediaUrl.replaceAll("\\{id\\}", id), Map.class);
				return new Image(String.valueOf(child.get("media_url")));
			}, executor)).collect(Collectors.toList());

			List<Image> images = imageFuture.stream().map(output -> output.join()).collect(Collectors.toList());
			return new ContentDTO(content, images);
		}, executor)).collect(Collectors.toList());

		return contentDTOs.stream().map(output -> output.join()).collect(Collectors.toList());
	}
}
