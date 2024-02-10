package com.instagl.controller;

import com.instagl.util.TypeUtil;
import com.instagl.dto.UserDTO;
import com.instagl.entity.Account;
import com.instagl.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login/")
@RequiredArgsConstructor
public class LoginController {
    @Value("${instagram.me.profile}")
    private String userProfileUrl;

    private final AccountService accountService;
    private final RestTemplate restTemplate = new RestTemplate();

    @CrossOrigin
    @PostMapping("google")
    public void getLoginInfoWithGoogle(@RequestBody Object info) throws InterruptedException, ParseException {
        //TODO 구글 로그인
    }

    @CrossOrigin
    @GetMapping("accessToken")
    public UserDTO loginWithInstagram(@RequestParam String code) throws InterruptedException, ParseException {
        MultiValueMap<String, Object> login = new LinkedMultiValueMap<>(); // for form data
        login.add("client_id", "1202988193978178");
        login.add("client_secret", "c05e2e3dd62e9c57ed074e28a50b9f72");
        login.add("grant_type", "authorization_code");
        login.add("redirect_uri", "https://localhost:3000/margatsni/login");
        login.add("client_id", "1202988193978178");
        login.add("code", code);
        Map<String, Object> responseByAccToken = restTemplate.exchange("https://api.instagram.com/oauth/access_token", HttpMethod.POST, new HttpEntity<>(login), TypeUtil.MAP)
                .getBody();

        System.out.println("response.toString() = " + responseByAccToken.toString());
        Optional<Object> optionalId = Optional.ofNullable(responseByAccToken.get("user_id"));

        if(optionalId.isEmpty()) {
            throw new IllegalStateException("계정 오류입니다.");
        }

        UriBuilder builder = UriComponentsBuilder.fromUriString(userProfileUrl + responseByAccToken.get("user_id").toString());
        builder.queryParam("fields", "id,username").queryParam("access_token", responseByAccToken.get("access_token"));

        Map<String, Object> responseUserName = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<>(login), TypeUtil.MAP)
                .getBody();

        Map<String, Object> profileInfo = accountService.getProfileInfo(responseUserName.get("username"));

        String id = optionalId.get().toString();

        // TODO 인스타 유저 고유ID Name 프로필사진(서버에 저장) Response -> account Table
        Account account = accountService.getAccountByFeedId(id)
                .orElseGet(() -> accountService.save(new Account(String.valueOf(responseUserName.get("username")), profileInfo.get("profilePicUrl").toString(), profileInfo.get("biography").toString(), Long.parseLong(profileInfo.get("following").toString()), Long.parseLong(profileInfo.get("follower").toString()), id, "", Boolean.FALSE)));

        return new UserDTO(id, responseByAccToken, account);
    }
}
