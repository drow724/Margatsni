package com.instagl.dto;

import com.instagl.entity.Content;

import com.instagl.entity.Image;
import com.instagl.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ContentDTO {

	private String caption;

	private LocationDTO location;

	private List<ImageDTO> images = new ArrayList<>();

	public ContentDTO(Content content, List<Image> images) {
		this.caption = content.getCaption();
		Location location = content.getLocation();
		this.location = new LocationDTO(location);
		this.images = images.stream().map(image -> new ImageDTO(image.getUrl())).collect(Collectors.toList());
	}
}
