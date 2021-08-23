package de.neuefische.rem_213_github.backend.config;

import de.neuefische.rem_213_github.backend.rest.github.GithubAPI;
import de.neuefische.rem_213_github.backend.rest.github.GithubClient;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubClientConfig {

    @Bean
    public GithubAPI getGithubAPI() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(GithubClient.class))
                .logLevel(Logger.Level.FULL)
                .target(GithubAPI.class, "https://api.github.com");
    }
}
