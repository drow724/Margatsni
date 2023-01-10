package com.instagl.scrapping;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.instagl.entity.Proxy;
import com.instagl.repository.ProxyRepository;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@EnableScheduling
@Service
@Slf4j
public class ProxyService {

	private final LaunchPersistentContextOptions launchOptions;
	
	private final ProxyRepository proxyRepository;
	
	private final Map<String, String> proxies = new ConcurrentHashMap<>();
	private final Map<String, String> failedProxies = new ConcurrentHashMap<>();
	
	@Transactional(readOnly = true)
	@PostConstruct
	public void init() {
		proxyRepository.findAll().parallelStream().forEach(proxy -> {
			proxies.put(proxy.getIp()+":"+proxy.getHost(), proxy.getIp()+":"+proxy.getHost());
		});
	}
	
	@Async
	@SuppressWarnings("unchecked")
	//@Scheduled(cron = "0 */1 * * * *")
	public void scheduledScrapingProxies() {
		log.info("proxies crawl start");
		try (Playwright playwright = Playwright.create()) {

			Browser browser = playwright.chromium().launch();

			Page page = browser.newPage();

			page.navigate("https://free-proxy-list.net/");

			List<String> proxies = (List<String>) page.evaluate(
				"""
					Object.values(document.evaluate("/html/body/section[1]/div/div[2]/div/table/tbody", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.children).filter(tr =>
					                   tr.children[6].innerText !== "no"
					).map(tr => tr.children[0].innerText + ":" + tr.children[1].innerText);
				""");
			
			log.info("scraping proxies size is {}",proxies.size());
			
			List<Proxy> list = new ArrayList<>();
			
			proxies.parallelStream().forEach(proxy -> {
				if(failedProxies.containsKey(proxy)) {
					log.info("{} already fail", proxy.split(":")[0], proxy.split(":")[1]);
				} else if(check(proxy)) {
					if(!this.proxies.containsKey(proxy)) {
						this.proxies.put(proxy,proxy);
						list.add(new Proxy(proxy.split(":")[0], proxy.split(":")[1]));
					}
				} else {
					failedProxies.put(proxy, proxy);
				}
			});
			
			log.info("valid proxies size is {}",this.proxies.size());

			proxyRepository.saveAll(list);
		}
	}
	
	public boolean check(String proxy) {
		System.setProperty("https.proxyHost", proxy.split(":")[0]);
		System.setProperty("https.proxyPort", proxy.split(":")[1]);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("https://www.instagram.com/").openConnection();
			connection.setConnectTimeout(10);
			connection.connect();
			log.info("{} success", proxy.split(":")[0], proxy.split(":")[1]);
			return true;
		} catch (Exception e) {
			log.info("{} fail", proxy.split(":")[0], proxy.split(":")[1]);	
			return false;
		}
	}

	public Collection<String> getProxies() {
		return proxies.values();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void remove(String proxy) {
		this.proxies.remove(proxy);
		failedProxies.put(proxy, proxy);
		proxyRepository.delete(new Proxy(proxy.split(":")[0], proxy.split(":")[1]));
	}
}
