package Security_JWT.demo.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 시큐리티 session을 만들어준다.(Security ContextHolder)
//오브젝트 => Authentication 타입 객체
//Authentication안에 User 정보가 있어야됨.
//User오브젝트 타입 => UserDetails 타입 객체

//Security Session => Authentication => UserDetails(PrincipalDetails)

import Security_JWT.demo.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String,Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }
    
    //OAuth 로그인
    public PrincipalDetails(User user, Map<String,Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }



    //해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        //이기능은 휴면계정이냐 아니냐를 판단할수 있다 예를들어
        //우리 사이트가 1년동안 회원이 로그인을 안하면 휴면계정으로 두기로 함
        //현재 시간 - 로그인 시간 => 1년을 초과하면 return false; 로 설정하면됨
        return true;
    }

    public User getUser() {
        return user;
    }


    /* OAuth2 오버라이드 */
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
