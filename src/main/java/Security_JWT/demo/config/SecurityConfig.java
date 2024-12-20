package Security_JWT.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                //1.csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                
                //2.인증 주소 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated() //인증되면 들어갈 수 있는 주소
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") //요청은 특정 권한 필요
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll() // 그 외 모든 요청은 인증 불필요
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm") // 로그인 페이지 URL 설정
                        .permitAll() // 로그인 페이지에 대한 접근 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트될 페이지 설정
                        .permitAll() // 로그아웃에 대한 접근 허용
                );

        return http.build();
    }
}

