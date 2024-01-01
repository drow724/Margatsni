package com.instagl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;

@Configuration
public class PlayWrightConfiguration {

	@Bean
	public BrowserType.LaunchOptions launchOptions() {
		BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
		launchOptions.headless = Boolean.TRUE;
		return launchOptions;
	}
	
}
