package com.instagl.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagl.dto.ContentDTO;
import com.microsoft.playwright.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.instagl.entity.Account;
import com.instagl.entity.Content;
import com.instagl.entity.Content.ContentBuilder;
import com.instagl.entity.Image;
import com.instagl.entity.Location;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ShareService {

	@Value("${instagram.me.media}")
	private String userMediaUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	private final ObjectMapper mapper = new ObjectMapper();

	private final BrowserType.LaunchOptions launchOptions;

	private final AccountService accountService;

	private final ContentServie contentServie;

	private final ImageService imageService;

	//@Retryable(maxAttempts = 3)
	public List<Location> getLocationInfo(String accessToken) throws InterruptedException, ParseException {
		System.out.println("userMediaUrl + accessToken = " + userMediaUrl + accessToken);
		Map res = restTemplate.getForObject(userMediaUrl + accessToken, Map.class);

		List<Map<String, Object>> data = (List<Map<String, Object>>) res.get("data");

		List<String> urls = data.stream().map(d -> String.valueOf(d.get("permalink"))).collect(Collectors.toList());

		List<CompletableFuture<Location>> dataList = urls.stream().map(url -> CompletableFuture.supplyAsync(() -> {
				Location locationDTO = null;
				try (Playwright playwright = Playwright.create()) {

					Browser context = playwright.chromium().launch(launchOptions);

					Page page = context.newPage();

					page.navigate(url);

					Response postResponse = page
							.waitForResponse(response -> response.url().contains("https://www.instagram.com/api/graphql"), () -> {
							});

					Map<String, Object> post = mapper.readValue(postResponse.text(), Map.class);

					Map<String, Object> postData = (Map<String, Object>) post.get("data");
					Map<String, Object> xdt_shortcode_media = (Map<String, Object>) postData.get("xdt_shortcode_media");
					Map<String, Object> location = (Map<String, Object>) xdt_shortcode_media.get("location");

					if(location == null) {
						return locationDTO;
					}
					page.navigate("https://www.instagram.com/explore/locations/" + location.get("id") + "/?img_index=1");

					Response locationResponse = page.waitForResponse(response -> response.url().contains("https://www.instagram.com/api/v1/locations/web_info/?"), () -> {
					});

					Map<String, Object> locationData = mapper.readValue(locationResponse.text(), Map.class);
					Map<String, Object> native_location_data = (Map<String, Object>) locationData.get("native_location_data");
					Map<String, Object> location_info = (Map<String, Object>) native_location_data.get("location_info");

					locationDTO = new Location(String.valueOf(location_info.get("location_address")), String.valueOf(location_info.get("name")),
							Double.parseDouble(location_info.get("lat").toString()),
							Double.parseDouble(location_info.get("lng").toString()));
				} catch (JsonMappingException e) {
					throw new RuntimeException(e);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
				return locationDTO;
			}
		)).collect(Collectors.toList());

		List<Location> locations = dataList.stream().map(output -> output.join()).collect(Collectors.toList());

		return locations;
	}
}
