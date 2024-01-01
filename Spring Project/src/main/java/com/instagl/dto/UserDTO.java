package com.instagl.dto;

import com.instagl.entity.Account;
import lombok.Getter;

import java.util.Map;

@Getter
public class UserDTO {

    private Long id;

    private String accessToken;

    private Boolean updating;

    public UserDTO(String id, Map<String, Object> response, Account account) {
        this.id = Long.parseLong(id);
        this.accessToken = response.get("access_token").toString();
        this.updating = account.getUpdating();
    }
}
