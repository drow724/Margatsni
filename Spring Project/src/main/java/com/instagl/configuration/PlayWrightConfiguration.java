package com.instagl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;

@Configuration
public class PlayWrightConfiguration {

	@Bean
	public LaunchPersistentContextOptions launchPersistentContextOptions() {
		LaunchPersistentContextOptions launchOptions = new BrowserType.LaunchPersistentContextOptions();
		launchOptions.headless = true;
		launchOptions.userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36";
		// launchOptions.setProxy(new Proxy("201.229.250.19:8080"));
		return launchOptions;
	}
	
}
