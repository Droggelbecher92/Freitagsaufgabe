package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.AccessToken;
import de.neuefische.rem_213_github.backend.api.Credentials;
import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.service.JwtService;
import de.neuefische.rem_213_github.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("me")
    public ResponseEntity<User> getLoggedInUser(@AuthenticationPrincipal UserEntity user){
        return ok(
                User.builder().name(user.getName()).build()
        );
    }

    @PostMapping("access_token")
    public ResponseEntity<AccessToken> getAccessToken(@RequestBody Credentials credentials){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword()
        );

        try {
            authenticationManager.authenticate(authToken);

            UserEntity user = userService.find(credentials.getUsername()).orElseThrow();

            String token = jwtService.createJwtToken(user);

            return ok(new AccessToken(token));
        }catch(AuthenticationException ex){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
