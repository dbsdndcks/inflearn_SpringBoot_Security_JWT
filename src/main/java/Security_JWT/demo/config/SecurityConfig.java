package Security_JWT.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated() // /user/** 요청은 인증 필요
                        .requestMatchers("/manager/**")
                        .hasAnyRole("ADMIN", "MANAGER") // /manager/** 요청은 특정 권한 필요
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll() // 그 외 모든 요청은 인증 불필요
                )
                .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 사용
                .logout(Customizer.withDefaults()); // 기본 로그아웃 설정

        return http.build();
    }
}

