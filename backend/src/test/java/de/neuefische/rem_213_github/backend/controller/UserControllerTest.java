package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.SpringBootTests;
import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.Optional;

import static de.neuefische.rem_213_github.backend.SpringTestConfig.MOCKED_USER_SERVICE_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles(MOCKED_USER_SERVICE_PROFILE)
public class UserControllerTest extends SpringBootTests {

    @Resource
    private UserController userController;

    @Resource
    private UserService userService; // with the profile active this service is a mock

    @Test
    public void testGet() {
        Optional<UserEntity> userEntityOptional = Optional.of(new UserEntity());

        String name = "name";
        when(userService.find(name)).thenReturn(userEntityOptional);

        ResponseEntity<User> userResponseEntity = userController.find(name);
        assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());

        userResponseEntity = userController.find("unknown");
        assertEquals(HttpStatus.NOT_FOUND, userResponseEntity.getStatusCode());
    }
}
