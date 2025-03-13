package org.example.expert.domain.common.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.entity.Log;
import org.example.expert.domain.common.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

  private final LogRepository logRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveLog(Long userId, String action, String message){
    Log log = new Log(userId, action, message);
    logRepository.save(log);
  }
}
