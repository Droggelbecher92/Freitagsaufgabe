package de.neuefische.rem_213_github.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rem213.nasa-client")
public class NasaClientConfigProperties {

    private String apiKey;
    private String accessToken;

    public String getApiKey() {
        return apiKey == null ? "DEMO_KEY" : apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAccessToken() {
        return accessToken == null ? "MY-Token" : accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
