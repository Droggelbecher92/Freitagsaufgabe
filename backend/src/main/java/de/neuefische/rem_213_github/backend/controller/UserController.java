package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.Password;
import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.api.CreatedUser;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static de.neuefische.rem_213_github.backend.controller.UserController.USER_CONTROLLER_TAG;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = USER_CONTROLLER_TAG, description = "Provides CRUD operations for an User")
@Api(
        tags = USER_CONTROLLER_TAG
)
@CrossOrigin
@RestController
@RequestMapping("/user")
@Getter
@Setter
public class UserController {

    public static final String USER_CONTROLLER_TAG = "User";

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE,consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_BAD_REQUEST, message = "Unable to create User with blank name"),
            @ApiResponse(code = SC_CONFLICT, message = "Unable to create User, user already exists")
    })
    public ResponseEntity<CreatedUser> create(@RequestBody User user) {
        try {
            UserEntity userEntity = map(user);
            String password = userService.generatePassword();
            userEntity.setPassword(userService.hashPassword(password));
            userEntity.setRole("USER");
            UserEntity createdUserEntity = userService.create(userEntity);
            CreatedUser createdUser = CreatedUser.builder()
                    .username(createdUserEntity.getName())
                    .role(createdUserEntity.getRole())
                    .avatarUrl(createdUserEntity.getAvatarUrl())
                    .password(password).build();

            return ok(createdUser);

        } catch (IllegalArgumentException e) {
            return badRequest().build();
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "{name}", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<User> find(@PathVariable String name) {
        Optional<UserEntity> userEntityOptional = userService.find(name);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            User user = map(userEntity);
            return ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NO_CONTENT, message = "No users found")
    })
    public ResponseEntity<List<User>> findAll() {
        List<UserEntity> userEntities = userService.findAll();
        if (userEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<User> users = map(userEntities);
        return ok(users);
    }

    @DeleteMapping(value = "{name}", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<User> delete(@PathVariable String name,@AuthenticationPrincipal UserEntity loggedInUser) {
        if(loggedInUser.getRole().equals("ADMIN")|| loggedInUser.getName().equals(name)){
            Optional<UserEntity> userEntityOptional = userService.delete(name);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                User user = map(userEntity);
                return ok(user);
            }
            return ResponseEntity.notFound().build();

        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @PutMapping(value = "/password",produces = APPLICATION_JSON_VALUE,consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_BAD_REQUEST, message = "Unable to create password with blank field"),
    })

    public ResponseEntity<CreatedUser> updatePassword(@RequestBody Password newPassword,@AuthenticationPrincipal UserEntity user){

        Optional<UserEntity> userEntityOptional = userService.updatePassword(user, newPassword.getPassword());
        if(userEntityOptional.isEmpty()){
            return   ResponseEntity.badRequest().build();
        }

        CreatedUser createdUser = map(userEntityOptional.get(),newPassword.getPassword());
        return ok(createdUser);

    }


    @PutMapping(value = "/{username}/reset-password",produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_NOT_FOUND, message = "Unable to create Password, user not found"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Admin-Password cannot be reset!")
    })

    public ResponseEntity<CreatedUser> resetPassword(@PathVariable String username){
        String password = userService.generatePassword();
        try {
            Optional<UserEntity> userEntityOptional = userService.resetPassword(username,password);
            CreatedUser createdUser = map(userEntityOptional.get(),password);
            return ok(createdUser);
        }
        catch (ResponseStatusException e) {
            if (e.getStatus().equals(HttpStatus.BAD_REQUEST)) {
                return ResponseEntity.badRequest().build();
            } else if (e.getStatus().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    private User map(UserEntity userEntity) {
        return User.builder()
                .name(userEntity.getName())
                .avatar(userEntity.getAvatarUrl())
                .build();
    }

    private UserEntity map(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setAvatarUrl(user.getAvatar());
        return userEntity;
    }

    private List<User> map(List<UserEntity> userEntities) {
        List<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            User user = map(userEntity);
            users.add(user);
        }
        return users;
    }

    private CreatedUser map(UserEntity userEntity, String password){
       return CreatedUser.builder()
                .username(userEntity.getName())
                .password(password)
                .role(userEntity.getRole())
                .avatarUrl(userEntity.getAvatarUrl())
                .build();
    }
}
