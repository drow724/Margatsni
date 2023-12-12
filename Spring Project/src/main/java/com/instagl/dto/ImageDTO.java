package com.instagl.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.instagl.entity.Content;
import com.instagl.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDTO {

	private String url;

}
