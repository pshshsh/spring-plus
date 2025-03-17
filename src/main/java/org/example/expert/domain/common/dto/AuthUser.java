package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
// 인증된 사용자의 정보를 담고 있는 역할 ,  사용자가 API요청시  JwtAuthenticationFilter에서 사용자가 담은 JWT 확인 후 AuthUser 생성
@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long id, String email, String nickname, UserRole role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }

}
