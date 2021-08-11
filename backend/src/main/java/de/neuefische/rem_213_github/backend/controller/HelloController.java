package de.neuefische.rem_213_github.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ok("<html><body><h1>Hello REM 21-3</h1></body></html>");
    }
}
