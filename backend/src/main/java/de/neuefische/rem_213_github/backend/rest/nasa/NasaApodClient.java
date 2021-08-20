package de.neuefische.rem_213_github.backend.rest.nasa;

import de.neuefische.rem_213_github.backend.config.NasaClientConfigProperties;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class NasaApodClient {

    private final NasaApodAPI nasaApodAPI;

    private NasaClientConfigProperties nasaClientConfigProperties;

    @Autowired
    public NasaApodClient(NasaClientConfigProperties nasaClientConfigProperties) {
        this.nasaClientConfigProperties = nasaClientConfigProperties;

        this.nasaApodAPI = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(NasaApodClient.class))
                .logLevel(Logger.Level.FULL)
                .target(NasaApodAPI.class, "https://api.nasa.gov");
    }

    public NasaApodDto getPictureOfTheDay() {
        return nasaApodAPI.getApodExample(nasaClientConfigProperties.getAccessToken(), nasaClientConfigProperties.getApiKey());
    }
}
