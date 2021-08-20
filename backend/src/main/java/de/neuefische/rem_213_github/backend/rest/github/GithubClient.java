package de.neuefische.rem_213_github.backend.rest.github;

import de.neuefische.rem_213_github.backend.config.GithubClientConfigProperties;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class GithubClient {

    private final GithubAPI githubAPI;
    private final GithubClientConfigProperties githubClientConfigProperties;

    @Autowired
    public GithubClient(GithubClientConfigProperties githubClientConfigProperties) {
        this.githubClientConfigProperties = githubClientConfigProperties;

        this.githubAPI = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(this.getClass()))
                .logLevel(Logger.Level.FULL)
                .target(GithubAPI.class, "https://api.github.com");
    }

    public GithubUserDto getWhoAmI() {
        return githubAPI.getUser(getAccessToken());
    }

    public GithubRepoDtos getUserRepos(String login) {
        List<GithubRepoDto> githubRepoDtoList = githubAPI.getUserRepos(getAccessToken(), login);

        GithubRepoDtos githubRepoDtos = new GithubRepoDtos();
        githubRepoDtos.setGithubRepoDtos(githubRepoDtoList);

        return githubRepoDtos;
    }

    public Optional<GithubPullDtos> getRepoPulls(String login, String repoName) {
        try {
            List<GithubPullDto> githubPullDtosList = githubAPI.getRepoPulls(getAccessToken(), login, repoName);
            if (githubPullDtosList != null && !githubPullDtosList.isEmpty()) {
                GithubPullDtos githubPullDtos = new GithubPullDtos();
                githubPullDtos.setGithubPullDtos(githubPullDtosList);

                return Optional.of(githubPullDtos);
            }
        } catch (FeignException e) {
            String errorMsg = String.format("No pulls found for repo=%s of user=%s", repoName, login);
            log.info(errorMsg);
            log.debug(String.format("%s : ex-msg %s", errorMsg, e.getMessage()));
        }
        return Optional.empty();
    }

    private String getAccessToken() {
        return githubClientConfigProperties.getAccessToken();
    }
}
