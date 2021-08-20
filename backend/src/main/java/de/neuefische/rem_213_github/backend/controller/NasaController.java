package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.rest.nasa.NasaApodClient;
import de.neuefische.rem_213_github.backend.rest.nasa.NasaApodDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.neuefische.rem_213_github.backend.controller.NasaController.NASA_CONTROLLER_TAG;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Tag(name = NASA_CONTROLLER_TAG, description = "Query Nasa APOD API")
@Api(
        tags = NASA_CONTROLLER_TAG
)
@RestController
@CrossOrigin
@RequestMapping("/nasa")
public class NasaController {

    public static final String NASA_CONTROLLER_TAG = "Nasa";

    private final NasaApodClient nasaApodClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NasaController(NasaApodClient nasaApodClient) {
        this.nasaApodClient = nasaApodClient;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Nasa Apod API")
    })
    public ResponseEntity<NasaApodDto> find() {
        NasaApodDto nasaApodDto = nasaApodClient.getPictureOfTheDay();


//        logger.trace("Hello Trace"); // --> very high details
//        logger.debug("Hello Debug"); // --> high details
//        logger.info("Hello Info"); // --> default
//        logger.warn("Hello Warn"); // --> attention
//        logger.error("Hello Error"); // --> failure

        logger.info(String.format("Query for nasa apod return %s", nasaApodDto));
        return ok(nasaApodDto);
    }
}
