package org.example.expert.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "log")
public class Log {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId; // 매니저 등록요청한 사람 ID


  private String action; // 수행한 작업

  private String message; // 상세 로그 메시지

  private LocalDateTime createdAt = LocalDateTime.now(); // 로그 생성 시간

  public Log(Long userId,  String action, String message) {
    this.userId = userId;
    this.action = action;
    this.message = message;
  }
}
