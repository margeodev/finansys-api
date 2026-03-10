package com.finansys.finansys_api.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${JWT_PUBLIC_KEY}")
  private String publicKeyEnv;

  @Value("${JWT_PRIVATE_KEY}")
  private String privateKeyEnv;

  @Value("${app.cors.allowed-origins}")
  private List<String> allowedOrigins;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/login", "/auth/refresh").permitAll()
                    .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(conf -> conf.jwt(jwt -> jwt.decoder(jwtDecoder)));

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(allowedOrigins);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  RSAPublicKey rsaPublicKey() throws Exception {
    return parsePublicKey(resolveKeyContent(publicKeyEnv));
  }

  @Bean
  RSAPrivateKey rsaPrivateKey() throws Exception {
    return parsePrivateKey(resolveKeyContent(privateKeyEnv));
  }

  @Bean
  JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  @Bean
  JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  // ---------- Métodos utilitários ----------

  /** Se o valor começar com "file:", lê o conteúdo do arquivo; senão retorna o próprio valor. */
  private String resolveKeyContent(String value) throws Exception {
    if (value != null && value.strip().startsWith("file:")) {
      String path = value.strip().substring(5).strip();
      return Files.readString(Path.of(path));
    }
    return value;
  }

  private RSAPublicKey parsePublicKey(String key) throws Exception {
    String cleanKey = key
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s+", "");
    byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
  }

  private RSAPrivateKey parsePrivateKey(String key) throws Exception {
    String cleanKey = key
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");
    byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
  }
}
