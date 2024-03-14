package com.instagl.service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagl.util.TypeUtil;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.instagl.entity.Account;
import com.instagl.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import static com.instagl.constant.AccountConstant.FILE_DIR_PATH;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;

	@Value("${instagram.craw.profile}")
	private String userCrawProfileUrl;

	private ExecutorService executor = Executors.newFixedThreadPool(100);
	private final RestTemplate restTemplate = new RestTemplate();

	private final ObjectMapper mapper = new ObjectMapper();

	private final BrowserType.LaunchOptions launchOptions;

	public Optional<Account> getAccount(Long id) {
		return accountRepository.findById(id);
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public Account changeUpdateState(String feedId) {
		Account account = accountRepository.findByFeedId(feedId).orElseThrow(() -> new NoSuchElementException("계정이 존재하지 않습니다."));
		account.changeUpdating();
		accountRepository.save(account);
		return account;
	}

    public Optional<Account> getAccountByFeedId(String feeId) {
		return accountRepository.findByFeedId(feeId);
    }

	// 프로필 정보 크롤링
	public Map<String, Object> getProfileInfo(Object nickName) {
		Map<String, Object> resultData = new HashMap<>();

		// Playwright Crawling
		try (Playwright playwright = Playwright.create();
			 Browser context = playwright.chromium().launch(launchOptions)) {
			Page page = context.newPage();

			page.navigate(userCrawProfileUrl.replaceAll("\\{nickname\\}", nickName.toString()));

			Response postResponse = page
					.waitForResponse(response -> response.url().contains(nickName.toString()), () -> {});

			Map<String, Object> post = mapper.readValue(postResponse.text(), TypeUtil.JSON_MAP);

			System.out.println("post = " + post);

			Map<String, Object> postData = (Map<String, Object>) post.get("data");
			Map<String, Object> userData = (Map<String, Object>) postData.get("user");

			if(userData == null) {
				throw new IllegalStateException("유저 데이터가 없습니다.");
			}

			Map<String, Object> following = (Map<String, Object>) userData.get("edge_followed_by");
			Map<String, Object> follower = (Map<String, Object>) userData.get("edge_follow");

			resultData.put("follower", follower.get("count"));
			resultData.put("following", following.get("count"));
			resultData.put("profilePicUrl", userData.get("profile_pic_url"));
			resultData.put("biography", userData.get("biography"));

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		return resultData;
    }

	public String saveProfileImageFile(String profileImageUrl, String fileName) {
		try (BufferedInputStream in = new BufferedInputStream(new URL(profileImageUrl).openStream());
			 FileOutputStream fileOutputStream = new FileOutputStream(FILE_DIR_PATH + fileName)) {

			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}

			in.close();
			fileOutputStream.close();

			return FILE_DIR_PATH + fileName;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
