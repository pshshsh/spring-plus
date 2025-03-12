package org.example.expert.config;

import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

// JWT 인증에서는 UserDetails를 사용하지 않기 때문에 AuthUser를 직접 저장
// 로그인 후 SecurityContextHolder에 저장되어 , 인증된 사용자의 정보 조회할 수 있도록 제공
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final AuthUser authUser;

  public JwtAuthenticationToken(AuthUser authUser) {
    super(authUser.getAuthorities()); // Spring Security의 권한 설정
    this.authUser = authUser;
    setAuthenticated(true); //  인증된 사용자로 설정
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return authUser;
  }
}
