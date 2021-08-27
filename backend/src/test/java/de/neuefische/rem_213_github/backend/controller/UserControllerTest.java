package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.api.CreatedUser;
import de.neuefische.rem_213_github.backend.config.JwtConfig;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

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
public class UserControllerTest{

    @LocalServerPort
    private int port;

    private String url(){
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
    public void testPostCreatedUserAdded(){
        // GIVEN
        String username = "BobAdmin";
        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofMinutes(jwtConfig.getExpiresAfterMinutes())));
        String token = Jwts.builder()
                .setClaims(new HashMap<>(
                        Map.of("role","ADMIN")
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
        assertThat(userService.find("maxi").get().getName(),is("maxi") );

    }
}
