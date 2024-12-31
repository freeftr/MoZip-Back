package com.ssafy.mozip.oauth2.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfo implements OAuthUserInfo {
    @JsonProperty("sub")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String picture;
}
