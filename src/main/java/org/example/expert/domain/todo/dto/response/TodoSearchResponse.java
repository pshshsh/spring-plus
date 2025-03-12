package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TodoSearchResponse {
  private final String title;
  private final long userCount;
  private final long commentCount;

  @QueryProjection // DTO도 Q파일로 생성
  public TodoSearchResponse(String title,long userCount, long commentCount){
    this.title = title;
    this.userCount = userCount;
    this.commentCount = commentCount;
  }
}
