package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.*;
import de.neuefische.rem_213_github.backend.config.JwtConfig;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import de.neuefische.rem_213_github.backend.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest(
        properties = "spring.profiles.active:h2",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private String url() {
        return "http://localhost:" + port + "/user";
    }

    @Resource
    private UserController userController;

    @Resource
    private UserService userService; // with the profile active this service is a mock

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private TestRestTemplate restTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Test
    public void testGetFindUserOk() {
        // GIVEN
        Optional<UserEntity> userEntityOptional = Optional.of(new UserEntity());

        String name = "name";
        when(userService.find(name)).thenReturn(userEntityOptional);

        // WHEN
        ResponseEntity<User> userResponseEntity = userController.find(name);

        // THEN
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
    }

    @Test
    public void testGetFindUserNotFound() {
        // GIVEN
        when(userService.find(anyString())).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<User> userResponseEntity = userController.find("unknown");

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
    }

    @Test
    public void testPostCreatedUserAdded() {
        // GIVEN
        String username = "BobAdmin";
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "ADMIN")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        User newUser = User.builder()
                .name("maxi")
                .avatar("nopic.com").build();


        // WHEN

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url(), HttpMethod.POST, new HttpEntity<>(newUser, headers), CreatedUser.class);
        // THEN

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getPassword().length(), is(10));
        assertThat(userService.find("maxi").get().getName(), is("maxi"));

    }

    @Test
    public void testPutPasswordIsUpdated() {
        // Given
        String username = "bill";
        String password = "12345";
        String role = "user";
        Password newPassword = Password.builder().password("1111").build();
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(
                UserEntity.builder()
                        .name(username)
                        .role(role)
                        .password(hashedPassword).build()
        );
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "USER")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/password";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.PUT, new HttpEntity<>(newPassword, headers), CreatedUser.class);

        // Then
        assertThat(response.getBody().getPassword() , is(newPassword.getPassword()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testPutPasswordUserNotFound() {
        // Given
        String username = "bill";
        Password newPassword = Password.builder().password("1111").build();

        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "USER")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/password";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.PUT, new HttpEntity<>(newPassword, headers), CreatedUser.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }


    @Test
    public void testResetPassword(){
        String username = "bill";
        String password = "12345";
        String role = "user";

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(
                UserEntity.builder()
                        .name(username)
                        .role(role)
                        .password(hashedPassword).build()
        );
        //who is logged in?
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "ADMIN")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject("max")
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/bill/reset-password";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.PUT, new HttpEntity<>(headers), CreatedUser.class);

        //Then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        CreatedUser actual = response.getBody();
            //check if user to be updated is really bill:
        assertThat(actual.getUsername(),is("bill"));

        assertThat(actual.getPassword(),not("12345"));

    }

    @Test
    public void deleteUserByAdmin(){

        //Given
        String username = "bill";
        String password = "12345";
        String role = "user";

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(
                UserEntity.builder()
                        .name(username)
                        .role(role)
                        .password(hashedPassword).build()
        );
        //who is logged in?
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "ADMIN")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject("max")
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/bill";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), CreatedUser.class);

        //Then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(userRepository.findByName("bill"),is(Optional.empty()));


    }


    @Test
    public void deleteUserByUserNotAllowed(){

        //Given

        String username = "bill";
        String password = "12345";
        String role = "user";

        String hashedPassword = passwordEncoder.encode(password);
        UserEntity user = UserEntity.builder()
                .name(username)
                .role(role)
                .password(hashedPassword).build();

        userRepository.save(user);


        //who is logged in?
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "USER")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject("max")
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/bill";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), CreatedUser.class);

        //Then
        assertThat(response.getStatusCode(),is(HttpStatus.UNAUTHORIZED));
        assertThat(userRepository.findByName("bill"),is(Optional.of(user)));


    }

    @Test
    public void deleteUserByUserAllowed(){

        //Given

        String username = "bill";
        String password = "12345";
        String role = "user";

        String hashedPassword = passwordEncoder.encode(password);
        UserEntity user = UserEntity.builder()
                .name(username)
                .role(role)
                .password(hashedPassword).build();

        userRepository.save(user);


        //who is logged in?
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role", "USER")
                ))
                .setIssuedAt(iat)
                .setExpiration(exp)
                .setSubject("bill")
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret()).compact();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        String url = url() + "/bill";
        ResponseEntity<CreatedUser> response = restTemplate
                .exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), CreatedUser.class);

        //Then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(userRepository.findByName("bill"),is(Optional.empty()));

    }

}
