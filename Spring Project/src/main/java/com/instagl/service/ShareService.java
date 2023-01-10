package com.instagl.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.instagl.entity.Account;
import com.instagl.entity.Content;
import com.instagl.entity.Content.ContentBuilder;
import com.instagl.entity.Image;
import com.instagl.entity.Location;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShareService {

	private final JSONParser parser = new JSONParser();

	private final LaunchPersistentContextOptions launchOptions;

	private final AccountService accountService;

	private final ContentServie contentServie;

	private final ImageService imageService;

	//@Retryable(maxAttempts = 3)
	public List<Content> getAccount(String username) throws InterruptedException, ParseException {

		List<Content> contents = new ArrayList<>();
		Response res = null;
		try (Playwright playwright = Playwright.create()) {

			BrowserContext context = playwright.chromium()
					.launchPersistentContext(Path.of("/instagl/src/main/resources"), launchOptions);

			Page page = context.newPage();

			page.navigate("https://www.instagram.com/" + username + "/");

			Response profileResponse = page
					.waitForResponse(response -> response.url().contains("?username=" + username), () -> {
						page.mouse().wheel(0D, 10000D);
						try {
							Thread.sleep(3000L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});

			JSONObject json = (JSONObject) parser.parse(profileResponse.text());

			String profilePicUrl = (String) ((JSONObject) ((JSONObject) json.get("data")).get("user"))
					.get("profile_pic_url");
			String biography = (String) ((JSONObject) ((JSONObject) json.get("data")).get("user")).get("biography");
			Long follow = (Long) ((JSONObject) ((JSONObject) ((JSONObject) json.get("data")).get("user"))
					.get("edge_follow")).get("count");
			Long followed = (Long) ((JSONObject) ((JSONObject) ((JSONObject) json.get("data")).get("user"))
					.get("edge_followed_by")).get("count");

			Account account = accountService.save(new Account(username, profilePicUrl, biography, follow, followed));

			while (true) {
				try {
					res = page.waitForResponse(response -> response.url().contains("api/v1/feed/user"), () -> {
						page.mouse().wheel(0D, 10000D);
						try {
							Thread.sleep(3000L);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				} catch (Exception e) {
					if ((boolean) page.evaluate(
							"Math.floor(document.documentElement.scrollHeight - document.documentElement.scrollTop) <= document.documentElement.clientHeight"))
						break;
				}

				contents.addAll(jsonParse(res, account));

				if ((boolean) page.evaluate(
						"Math.floor(document.documentElement.scrollHeight - document.documentElement.scrollTop) <= document.documentElement.clientHeight"))
					break;
			}

			return contents;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Content> jsonParse(Response response, Account account) {

		Set<Long> set = new HashSet<>();
		List<Content> contents = new ArrayList<>();
		try {
			JSONObject json = (JSONObject) parser.parse(response.text());
			if (json.get("items") != null) {
				JSONArray items = (JSONArray) json.get("items");
				items.forEach(item -> {
					Map<String, Object> map = (Map<String, Object>) item;
					Map<String, Object> data = (Map<String, Object>) map.get("location");
					if (map.get("location") != null && set.add(Long.parseLong((String) map.get("pk"))) && (Double) data.get("lat") != null && (Double) data.get("lng") != null) {

						ContentBuilder builder = Content.builder();

						Location location = Location.builder().address((String) data.get("address"))
								.city((String) data.get("city")).name((String) data.get("name"))
								.lat((Double) data.get("lat")).lng((Double) data.get("lng")).build();
						builder.location(location);

						if (map.get("caption") != null) {
							Map<String, Object> caption = (Map<String, Object>) map.get("caption");
							builder.caption((String) caption.get("text"));
						}

						List<Image> images = new ArrayList<>();

						if (map.get("carousel_media") != null) {
							((List<Map<String, Object>>) map.get("carousel_media")).forEach(media -> {
								String url = (String) ((List<Map<String, Object>>) ((Map<String, Object>) media
										.get("image_versions2")).get("candidates")).get(0).get("url");
								images.add(new Image(url));
							});
						} else {
							String url = (String) ((List<Map<String, Object>>) ((Map<String, Object>) map
									.get("image_versions2")).get("candidates")).get(0).get("url");
							images.add(new Image(url));
						}

						builder.images(images);
						builder.account(account);

						Content content = contentServie.save(builder.build());
						contents.add(content);

						imageService.saveAll(images.stream().map(image -> {
							image.setContent(content);
							return image;
						}).collect(Collectors.toList()));
					}
				});
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contents;
	}
}
