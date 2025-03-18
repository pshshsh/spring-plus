package org.example.expert.domain.common.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;


  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<String> uploadImage(@RequestPart MultipartFile file) throws IOException {
    String imageUrl = s3Service.uploadFile(file);
    return ResponseEntity.ok(imageUrl);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteImage(@RequestParam String fileName) {
    s3Service.deleteFile(fileName);
    return ResponseEntity.ok("삭제: " + fileName);
  }
}
