package org.example.expert.domain.common.service;

import com.amazonaws.auth.AWSCredentials;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

  private S3Client s3Client;

  @Value("${aws.credentials.access-key}")
  private String accessKey;

  @Value("${aws.credentials.secret-key}")
  private String secretKey;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.region}")
  private String region;

  @PostConstruct
  public void init() {
    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();

    s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

    return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
  }


}