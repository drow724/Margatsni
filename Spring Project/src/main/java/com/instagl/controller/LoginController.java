package com.instagl.controller;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();

    @CrossOrigin
    @PostMapping("google")
    public void getLoginInfoWithGoogle(@RequestBody Object info) throws InterruptedException, ParseException {
        //TODO 구글 로그인
    }
    @CrossOrigin
    @GetMapping("accessToken")
    public Map<String, Object> loginWithInstagram(@RequestParam String code) throws InterruptedException, ParseException {
        MultiValueMap<String, Object> login = new LinkedMultiValueMap<>();
        login.add("client_id", "1202988193978178");
        login.add("client_secret", "c05e2e3dd62e9c57ed074e28a50b9f72");
        login.add("grant_type", "authorization_code");
        login.add("redirect_uri", "https://localhost:3000/margatsni/login");
        login.add("client_id", "1202988193978178");
        login.add("code", code);
        Map<String, Object> response = restTemplate.postForObject("https://api.instagram.com/oauth/access_token", login, Map.class);
        return response;
    }
}
