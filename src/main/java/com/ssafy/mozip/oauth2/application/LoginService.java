package com.ssafy.mozip.oauth2.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import com.ssafy.mozip.oauth2.domain.AuthTokens;
import com.ssafy.mozip.oauth2.domain.RefreshToken;
import com.ssafy.mozip.oauth2.domain.RefreshTokenRepository;
import com.ssafy.mozip.oauth2.dto.request.GoogleLoginRequest;
import com.ssafy.mozip.oauth2.infrastructure.GoogleOAuthProvider;
import com.ssafy.mozip.oauth2.infrastructure.OAuthUserInfo;
import com.ssafy.mozip.oauth2.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final GoogleOAuthProvider googleOAuthProvider;

    public AuthTokens googleLogin(GoogleLoginRequest googleLoginRequest) {
        String googleAccessToken = googleOAuthProvider.fetchAccessToken(googleLoginRequest.code());
        OAuthUserInfo userInfo = googleOAuthProvider.getUserInfo(googleAccessToken);

        Member member = findOrCreateMember(
                userInfo.getSocialId(),
                userInfo.getName(),
                userInfo.getProfileImageUrl(),
                userInfo.getEmail()
        );

        AuthTokens authTokens = jwtUtil.createAuthToken(member.getId().toString());
        RefreshToken refreshToken = new RefreshToken(member.getId(), authTokens.refreshToken());
        refreshTokenRepository.save(refreshToken);

        return authTokens;
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public String reissueAccessToken (String refreshToken, String authHeader) {
        String accessToken = authHeader.split( " ")[1];

        jwtUtil.validateRefreshToken(refreshToken);

        if(jwtUtil.isAccessTokenExpired(accessToken)) {
            return accessToken;
        }

        if(jwtUtil.isAccessTokenValid(accessToken)) {
            return accessToken;
        }

        RefreshToken foundRefreshToken =
                refreshTokenRepository.findById(refreshToken)
                        .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN));
        return jwtUtil.reissueAccessToken(foundRefreshToken.getUserId().toString());
    }

    private Member findOrCreateMember(String socialId, String name, String profileImageUrl, String email) {
        return memberRepository.findBySocialId(socialId)
                .orElseGet(() -> createMember(socialId, name, profileImageUrl, email));
    }

    private Member createMember(String socialId, String name, String profileImageUrl, String email) {
        log.info("Created new user: {}", name);
        return memberRepository.save(Member.of(socialId, name, profileImageUrl, email));
    }
}
