package com.instagl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;
import java.util.Map;

public class TypeUtil {

    public final static ParameterizedTypeReference<Map<String, Object>> MAP = new ParameterizedTypeReference<>() {};

    public final static TypeReference<Map<String, Object>> JSON_MAP = new TypeReference<>() {};
}
