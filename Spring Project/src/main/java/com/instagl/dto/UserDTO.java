package com.instagl.dto;

import com.instagl.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class UserDTO {

    private String feedId;

    private String accessToken;

    private Boolean updating;

    public UserDTO(String feedId, Map<String, Object> response, Account account) {
        this.feedId = feedId;
        this.accessToken = response.get("access_token").toString();
        this.updating = account.getUpdating();
    }
}
