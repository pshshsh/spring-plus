package org.example.expert.domain.todo.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;

import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<Todo> findByIdWithUser(Long todoId) {
    QTodo todo = QTodo.todo;
    QUser user = QUser.user;

    return Optional.ofNullable(
        jpaQueryFactory
            .select(todo)
            .from(todo)
            .leftJoin(todo.user, user).fetchJoin() // N+1 방지
            .where(todo.id.eq(todoId))
            .fetchOne() //단건 조회
    );
  }

  @Override
  public Page<TodoSearchResponse> getSearchTodos(
      String title, LocalDateTime startDate, LocalDateTime endDate,
      String nickname, Pageable pageable) {
      QTodo todo = QTodo.todo;
      QManager manager = QManager.manager;
      QComment comment = QComment.comment;

    List<TodoSearchResponse> response = jpaQueryFactory.select(new QTodoSearchResponse(
            todo.title,
            manager.id.countDistinct(),
            comment.id.count()
        ))
        .from(todo)
        .leftJoin(todo.managers, manager)
        .leftJoin(todo.comments, comment)
        .where(
            title != null ? todo.title.contains(title) : null,
            startDate != null ? todo.createdAt.goe(startDate) : null,
            endDate != null ? todo.createdAt.loe(endDate) : null,
            nickname != null ? manager.user.nickname.contains(nickname) : null
        )
        .groupBy(todo.id)
        .orderBy(todo.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    //  전체 개수 조회
    long total = Optional.ofNullable(jpaQueryFactory
        .select(todo.count())
        .from(todo)
        .where(
            title != null ? todo.title.contains(title) : null,
            startDate != null ? todo.createdAt.goe(startDate) : null,
            endDate != null ? todo.createdAt.loe(endDate) : null,
            nickname != null ? manager.user.nickname.contains(nickname) : null
        )
        .fetchOne()).orElse(0L);

    //
    return PageableExecutionUtils.getPage(response, pageable, () -> total);
  }
}