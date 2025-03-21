package org.example.expert.domain.user.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  private List<User> users;

  @BeforeEach
   void setupEach(){
    var value = new AtomicInteger(0);

   users =  FixtureMonkey.builder().build()
        .giveMeBuilder(User.class)
        .setNull("id")
        .setLazy("email", () -> "test" + value.incrementAndGet() + "@gmail.com")
        .setLazy("nickname", () -> "nickname" + value.incrementAndGet())
        .set("password", "password123")
        .set("userRole", UserRole.ROLE_USER)
        .sampleList(1000000);


  }


  @Test
  void User를_NickName으로_조회할_수_있다() {
  }
}