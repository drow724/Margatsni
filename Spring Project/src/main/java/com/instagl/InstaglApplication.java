package com.instagl;

import java.io.File;
import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import com.instagl.scrapping.ProxyService;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@EnableRetry
@EnableCaching
@SpringBootApplication
@RequiredArgsConstructor
public class InstaglApplication {

	private final LaunchPersistentContextOptions launchOptions;
	private final ProxyService proxyService;

	public static void main(String[] args) {
		SpringApplication.run(InstaglApplication.class, args);
	}
	
	@PostConstruct
	public void setup() throws InterruptedException {

		// 파일 경로 및 파일명 지정
		File file = new File("users" + File.separator + "testFile.txt");

		if (file.mkdir()) {
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
		}

		try (Playwright playwright = Playwright.create()) {

//			for (String proxy : proxyService.getProxies()) {
//				if (proxyService.check(proxy)) {
//					System.setProperty("https.proxyHost", proxy.split(":")[0]);
//					System.setProperty("https.proxyPort", proxy.split(":")[1]);
//					break;
//				}
//			}

			BrowserContext context = playwright.chromium()
					.launchPersistentContext(Path.of("/instagl/src/main/resources"), launchOptions);

			Page page = context.newPage();

			page.navigate("https://www.instagram.com/");

			Thread.sleep(5000L);

			if (page.locator(
					"xpath=/html/body/div[2]/div/div/div/div[1]/div/div/div/div[1]/section/main/article/div[2]/div[1]/div[2]/form/div/div[1]/div/label/input")
					.isVisible()) {
				page.locator(
						"xpath=/html/body/div[2]/div/div/div/div[1]/div/div/div/div[1]/section/main/article/div[2]/div[1]/div[2]/form/div/div[1]/div/label/input")
						.fill("se_unit");
				page.locator(
						"xpath=/html/body/div[2]/div/div/div/div[1]/div/div/div/div[1]/section/main/article/div[2]/div[1]/div[2]/form/div/div[2]/div/label/input")
						.fill("!!aa119562");
				page.locator(
						"xpath=/html/body/div[2]/div/div/div/div[1]/div/div/div/div[1]/section/main/article/div[2]/div[1]/div[2]/form/div/div[2]/div/label/input")
						.press("Enter");
				Thread.sleep(5000L);
			}

			page.close();
			context.close();
		}
	}
}
