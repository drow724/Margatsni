package com.instagl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class RestTemplateUtil {

    public static final HttpEntity<?> DEFAULT_HTTP_ENTITY = new HttpEntity<>(new HttpHeaders());
}
