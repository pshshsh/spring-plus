package org.example.expert.domain.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

  @GetMapping
  public Map<String,String> checkHealth(){
    return Map.of("server", "실행중");
  }
}
