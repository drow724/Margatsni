package com.instagl;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.instagl.entity.Location;

@Component
public class UsernameCache {

	private final Map<String, List<Location>> cache = new ConcurrentHashMap<>();
	
	public List<Location> getLocations(String username) throws NoSuchElementException {
		if(!cache.containsKey(username)) {
			throw new NoSuchElementException();
		}
		return cache.get(username);
	}
	
	public void insertLocations(String username, List<Location> locations) {
		cache.put(username, locations);
	}
}
