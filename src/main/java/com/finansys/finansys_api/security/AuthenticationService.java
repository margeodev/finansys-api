package com.finansys.finansys_api.security;
import com.finansys.finansys_api.domain.dto.response.AuthenticationResponse;
import com.finansys.finansys_api.domain.model.RefreshToken;
import com.finansys.finansys_api.domain.model.User;
import com.finansys.finansys_api.domain.service.RefreshTokenService;
import com.finansys.finansys_api.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;
  public AuthenticationResponse authenticate(Authentication authentication) {
    String accessToken = jwtService.generateAccessToken(authentication);

    // Busca o usuário pelo email (que é o username no sistema)
    User user = userService.findByUserName(authentication.getName());

    // Cria refresh token
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

    return new AuthenticationResponse(
            accessToken,
            refreshToken.getToken(),
            "Bearer",
            jwtService.getAccessTokenExpiration()
    );
  }

  public AuthenticationResponse refreshToken(String refreshTokenValue) {
    RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue)
            .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

    refreshToken = refreshTokenService.verifyExpiration(refreshToken);

    User user = refreshToken.getUser();

    // Gera novo access token
    UserAuthenticated userAuth = new UserAuthenticated(user);
    String newAccessToken = jwtService.generateAccessToken(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, userAuth.getAuthorities()
            )
    );

    // Opcionalmente, pode gerar um novo refresh token
    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

    return new AuthenticationResponse(
            newAccessToken,
            newRefreshToken.getToken(),
            "Bearer",
            jwtService.getAccessTokenExpiration()
    );
  }
}