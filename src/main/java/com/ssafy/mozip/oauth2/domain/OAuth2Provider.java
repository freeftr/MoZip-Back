package com.ssafy.mozip.oauth2.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    GOOGLE("google");

    private final String registrationId;
}
