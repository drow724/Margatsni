package com.instagl.dto;

import com.instagl.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class UserDTO {

    private String feedId;

    private String accessToken;

    private Boolean updating;

    private Boolean enableUpdating;

    public UserDTO(String feedId, Map<String, Object> response, Account account) {
        this.feedId = feedId;
        this.accessToken = response.get("access_token").toString();
        this.updating = account.getUpdating();

        Optional<LocalDateTime> lastModifyingDateOpt = Optional.ofNullable(account.getLastModifyingDate());
        lastModifyingDateOpt.ifPresent(lastModifyingDate -> {
            this.enableUpdating = LocalDateTime.now().isAfter(lastModifyingDate.plusWeeks(1L));
        });
    }

    public UserDTO(String feedId, String accessToken, Account account) {
        this.feedId = feedId;
        this.accessToken = accessToken;
        this.updating = account.getUpdating();

        Optional<LocalDateTime> lastModifyingDateOpt = Optional.ofNullable(account.getLastModifyingDate());
        lastModifyingDateOpt.ifPresent(lastModifyingDate -> {
            System.out.println("lastModifyingDate = " + lastModifyingDate);
            System.out.println("lastModifyingDate.plusWeeks(1L) = " + lastModifyingDate.plusWeeks(1L));
            System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
            this.enableUpdating = LocalDateTime.now().isAfter(lastModifyingDate.plusWeeks(1L));
        });
    }
}
