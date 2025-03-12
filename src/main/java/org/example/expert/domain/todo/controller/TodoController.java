package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponse> saveTodo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TodoSaveRequest todoSaveRequest
    ) {
        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            @RequestParam(required = false) String weather,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        LocalDateTime startDateTime = startDate != null ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? LocalDate.parse(endDate).atTime(23, 59, 59) : null;
        return ResponseEntity.ok(todoService.getTodos(weather,startDateTime,endDateTime,page, size));
    }

    // Level 3.10 새로운 api
    @GetMapping("/todos/search")
    public ResponseEntity<Page<TodoSearchResponse>> getSerchTodos(
        @RequestParam(required = false) String title,
        @RequestParam(name = "startDate", required = false) String startDate,
        @RequestParam(name = "endDate", required = false) String endDate,
        @RequestParam(required = false) String nickname,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        LocalDateTime startDateTime = startDate != null ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? LocalDate.parse(endDate).atTime(23, 59, 59) : null;

        return ResponseEntity.ok(
            todoService.getSearchTodos(title, startDateTime, endDateTime, nickname, page, size)
        );
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }



}
