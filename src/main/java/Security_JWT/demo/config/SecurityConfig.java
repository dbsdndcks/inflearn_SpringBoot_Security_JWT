package Security_JWT.demo.config;

//구글 로그인이 완료된 뒤의 후처리가 필요함. 1.코드받기(인증), 2.엑세스토큰(권한),
// 3.사용자프로필 정보를 가져오고
// 4-1.그정보를토대로 회원가입을 자동으로 진행시키키도함
// 4-2.(이메일,전화번호,이름,아이디) 쇼핑몰 -> (집주소) 백화점몰 -> (vip등급,일반등급)

import Security_JWT.demo.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured, @PreAuthorize 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService) // 사용자 정보 후처리 서비스 설정
                        )
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
