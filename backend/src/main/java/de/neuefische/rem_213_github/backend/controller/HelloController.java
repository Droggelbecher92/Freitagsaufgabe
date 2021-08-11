package de.neuefische.rem_213_github.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static de.neuefische.rem_213_github.backend.controller.HelloController.HELLO_TAG;
import static org.springframework.http.ResponseEntity.ok;

@Api(
        tags = HELLO_TAG
)
@CrossOrigin
@RestController
@Tag(name = HELLO_TAG, description = "This call only says Hello to REM-21-3!")
public class HelloController {

    public static final String HELLO_TAG = "Hello";

    @ApiOperation(value = "Say hello to REM 21-3", hidden = true)
    @GetMapping("/")
    public ResponseEntity<String> hello() {
        String cssStyle = "position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);";
        String swaggerLink = "/api/rem213/swagger-ui/";

        return ok(String.format(
                "<html><body><h1 style=\"%s\"><a href=\"%s\" alt=\"\">Swagger REM 21-3</a></h1></body></html>",
                cssStyle, swaggerLink
        ));
    }
}
