package cart.ticket.authservice.infrastructure.security;

import cart.ticket.authservice.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final JwtProvider jwtTokenProvider;

  public SecurityConfig(UserService userService, JwtProvider jwtTokenProvider) {
    this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/signin")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/oauth2/redirect/**", "/oauth2/callback/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            new JwtFilter(jwtTokenProvider, userService),
            UsernamePasswordAuthenticationFilter
                .class); // The last filter will not reach, only used for setting Jwt Filter before
    // other filter.

    return http.build();
  }
}
