package com.ssafy.mozip.oauth2.infrastructure;

public interface OAuthUserInfo {
    String getId();
    String getEmail();
    String getName();
    String getPicture();
}

