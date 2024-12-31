package com.ssafy.mozip.oauth2.infrastructure;

public interface OAuthProvider {
    String fetchAccessToken(String code);
    OAuthUserInfo getUserInfo(String accessToken);
}
