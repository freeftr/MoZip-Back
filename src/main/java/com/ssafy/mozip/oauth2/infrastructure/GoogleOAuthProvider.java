package com.ssafy.mozip.oauth2.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class GoogleOAuthProvider implements OAuthProvider {

    private final RestTemplate restTemplate;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";

    public GoogleOAuthProvider(
            RestTemplate restTemplate,
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}")String clientSecret,
            @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")String redirectUri)
    {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public String fetchAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
                GOOGLE_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                GoogleTokenResponse.class
        );

        log.info("Received token for code : {}", code);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.UNABLE_TO_GET_ACCESS_TOKEN))
                .getAccessToken();
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                USER_INFO_URI,
                HttpMethod.GET,
                requestEntity,
                GoogleUserInfo.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new BadRequestException(ExceptionCode.UNABLE_TO_GET_USER_INFO);
    }

    @Getter
    public static class GoogleTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("expires_in")
        private Integer expiresIn;
    }
}
