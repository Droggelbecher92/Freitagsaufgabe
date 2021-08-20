package de.neuefische.rem_213_github.backend.rest.nasa;


import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers(
    "Authorization: Bearer {access-token}"
)
public interface NasaApodAPI {

    @RequestLine("GET /planetary/apod?api_key={api-key}")
    NasaApodDto getApodExample(@Param("access-token") String accessToken, @Param("api-key") String key);
}
