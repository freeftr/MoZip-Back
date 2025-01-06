package com.ssafy.mozip.oauth2.presentation;

import com.ssafy.mozip.oauth2.application.LoginService;
import com.ssafy.mozip.oauth2.domain.AuthTokens;
import com.ssafy.mozip.oauth2.dto.request.GoogleLoginRequest;
import com.ssafy.mozip.oauth2.dto.response.AccessTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private static final int ONE_WEEK_SECONDS = 604800;

    private final LoginService loginService;

    @PostMapping("/login/google")
    public ResponseEntity<AccessTokenResponse> googleLogin(
            @RequestBody GoogleLoginRequest googleLoginRequest,
            HttpServletResponse response
    ) {
        AuthTokens authTokens = loginService.googleLogin(googleLoginRequest);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", authTokens.refreshToken())
                .maxAge(ONE_WEEK_SECONDS)
                .httpOnly(true)
                .sameSite("None")
                .domain(".simplesns.com")
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new AccessTokenResponse(authTokens.accessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenResponse> reissueToken(
            @CookieValue("refresh-token") String refreshToken,
            @RequestHeader("Authorization") String authHeader
    ) {
        String reissuedToken = loginService.reissueAccessToken(refreshToken, authHeader);
        return ResponseEntity.ok(new AccessTokenResponse(reissuedToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("refresh-token") String refreshToken
    ) {
        loginService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }
}
