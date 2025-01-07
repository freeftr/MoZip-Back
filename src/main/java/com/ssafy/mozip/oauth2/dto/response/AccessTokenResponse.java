package com.ssafy.mozip.oauth2.dto.response;

public record AccessTokenResponse(
        String accessToken
) {
    public static AccessTokenResponse from(
            String accessToken
    ) {
        return new AccessTokenResponse(accessToken);
    }
}
