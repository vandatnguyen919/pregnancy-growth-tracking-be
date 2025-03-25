package com.pregnancy.edu.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfiguration {

    private final RSAPublicKey publicKey;

    private final RSAPrivateKey privateKey;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;

    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    private final OAuth2LoginFailureHandler oauth2LoginFailureHandler;

    private final String baseUrl = "/api/v1";

    public SecurityConfiguration(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler, OAuth2LoginSuccessHandler oauth2LoginSuccessHandler, OAuth2LoginFailureHandler oauth2LoginFailureHandler) throws NoSuchAlgorithmException {
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.oauth2LoginFailureHandler = oauth2LoginFailureHandler;

        // Generate a public/private key pair.
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // The generated key will have a size of 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/auth/login").authenticated() // Protect this endpoint
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/otp/**").permitAll()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAuthority("ROLE_admin") // Protect this endpoint
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").hasAuthority("ROLE_admin") // Protect this endpoint
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/**").authenticated() // Protect this endpoint
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/**").hasAuthority("ROLE_admin") // Protect this endpoint
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/blog-posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/blog-posts/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/blog-posts/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/blog-posts/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/tags/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/tags").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/tags/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/tags/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/blog-comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/blog-comments").authenticated()
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/blog-comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/blog-comments/**").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/blog-likes").authenticated()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/blog-likes").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/membership-plans/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/membership-plans").authenticated()
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/membership-plans/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/membership-plans/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/subscriptions/**").authenticated()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/subscriptions/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/membership-plans/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/membership-plans/**").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/payment/create-payment").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/payment/check-payment/vnpay").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/standards/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/v1/standards").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/v1/standards/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/standards/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/metrics/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/v1/metrics").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/v1/metrics/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/metrics/**").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/fetuses/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/v1/fetuses").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/v1/fetuses/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/fetuses/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/pregnancies/**").authenticated()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/v1/pregnancies").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/v1/pregnancies/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/pregnancies/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/fetus-metrics/**").hasAuthority("ROLE_member")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/v1/fetus-metrics/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/v1/fetus-metrics/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/fetus-metrics/**").authenticated()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/v1/reminders/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/fetus-metrics/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/v1/fetus-metrics/**").authenticated()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html").permitAll()
                        // Disallow everything else.
                        .anyRequest().authenticated() // Always a good idea to put this as last.
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // This is for H2 browser console access
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(this.customBasicAuthenticationEntryPoint)
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(customBearerTokenAccessDeniedHandler)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler(oauth2LoginFailureHandler)
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
