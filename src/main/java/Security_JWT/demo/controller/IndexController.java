package Security_JWT.demo.controller;

import Security_JWT.demo.model.User;
import Security_JWT.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 리턴하겠다!
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        //머스테치 기본폴더str/main/resources/
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        // 인증 정보를 가져오는 방법
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 사용자일 경우
        if (authentication == null
                || !((org.springframework.security.core.Authentication) authentication).isAuthenticated()) {
            System.out.println("사용자는 인증되지 않았습니다.");
        } else {
            // 인증된 사용자 정보
            System.out.println("인증된 사용자: " + authentication.getName());
        }

        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); //회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인 할 수 없음 이유는 패스워드가 암호화가 안되었다.
        return "redirect:/loginForm";
    }

}
