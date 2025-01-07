package com.ssafy.mozip.oauth2.infrastructure;

public interface OAuthUserInfo {
    String getSocialId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
}

