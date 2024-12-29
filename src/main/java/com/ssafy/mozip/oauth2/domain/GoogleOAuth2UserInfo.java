package com.ssafy.mozip.oauth2.domain;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String name;
    private final String email;
    private final String profileImageUrl;

    public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
        this.id = attributes.get("sub").toString();
        this.name = attributes.get("name").toString();
        this.email = attributes.get("email").toString();
        this.profileImageUrl = attributes.get("profile_image_url").toString();
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }


}
