package Security_JWT.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured, @PreAuthorize 활성화
public class SecurityConfig {

    // BCryptPasswordEncoder 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain 빈 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // 2. 인증 및 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated() // 인증만 필요
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // 특정 권한 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능
                        .anyRequest().permitAll() // 그 외는 접근 허용
                )

                // 3. 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/loginForm") // 커스텀 로그인 페이지
                        .loginProcessingUrl("/login") // 로그인 처리 URL
                        .defaultSuccessUrl("/", true) // 성공 시 이동할 기본 URL
                        .permitAll() // 로그인 페이지 접근 허용
                )

                // 4. OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginForm") // OAuth2 로그인 페이지
                        .permitAll() // OAuth2 로그인 페이지 접근 허용
                )

                // 5. 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트 URL
                        .permitAll() // 로그아웃 접근 허용
                );

        return http.build();
    }
}
