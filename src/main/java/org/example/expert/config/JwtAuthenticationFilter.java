package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpRequest,
      @NonNull HttpServletResponse httpResponse,
      @NonNull FilterChain chain
  ) throws ServletException, IOException {
    String authorizationHeader = httpRequest.getHeader("Authorization"); // 클라이언트가 헤더에 JWT 토큰 포함하여 요청 보냄

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // JWT가 Bearer로 시작하는지 확인
      String jwt = jwtUtil.substringToken(authorizationHeader); //JWT를 substringToken()으로 추출
      try {
        Claims claims = jwtUtil.extractClaims(jwt); // JWT 유효성 검증

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          setAuthentication(claims); // 토큰이 유효하면 SecurityContexHolder에 사용자 정보 저장
        }
      } catch (SecurityException | MalformedJwtException e) {
        log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
        log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
        log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
        httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
      } catch (Exception e) {
        log.error("Internal server error", e);
        httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
    chain.doFilter(httpRequest, httpResponse); //다음 필터로 요청 전달
  }

  private void setAuthentication(Claims claims) {
    Long userId = Long.valueOf(claims.getSubject()); // 사용자 ID 추출
    String email = claims.get("email", String.class); // 이메일 추출
    String nickname = claims.get("nickname", String.class); // 닉네임 추출
    UserRole userRole = UserRole.of(claims.get("userRole", String.class)); // 역할 추출

    AuthUser authUser = new AuthUser(userId, email,nickname,userRole); //AuthUser 객체 생성
    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken); // SecurityContextHolder에 authenticationTkoen 저장하여 인증 완료
  }
}

