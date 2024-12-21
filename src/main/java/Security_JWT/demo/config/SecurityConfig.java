package Security_JWT.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig{

    //해당 메서드의 리턴되는 오브젝트를 ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder endcodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                //1.csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                //2.인증 주소 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") //요청은 특정 권한 필요
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll() // 그 외 모든 요청은 인증 불필요
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // /login주소가 호출이 되면시큐리티가 낚아채서 대신 로그인을 진행해준다.
                        .defaultSuccessUrl("/")
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

