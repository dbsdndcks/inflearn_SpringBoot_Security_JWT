package Security_JWT.demo.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest.getClientRegistration());
        System.out.println("userRequest : " + userRequest.getAccessToken());

        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> CODE를 리턴 (OAuth-Clinet라이브러리) -> AccessToken요청
        //userRequest 정보 -> loadUser 함수 ->구글로부터 회원프로필 받아준다.
        System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //회원가입을 강제로 진행해볼 예정
        return super.loadUser(userRequest);
    }

}
