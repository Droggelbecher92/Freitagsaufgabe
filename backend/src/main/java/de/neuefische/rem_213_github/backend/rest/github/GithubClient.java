package de.neuefische.rem_213_github.backend.rest.github;

import de.neuefische.rem_213_github.backend.config.GithubClientConfigProperties;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class GithubClient {

    private final GithubClientConfigProperties githubClientConfigProperties;
    private final GithubAPI githubAPI;

    @Autowired
    public GithubClient(GithubClientConfigProperties githubClientConfigProperties, GithubAPI githubAPI) {
        this.githubClientConfigProperties = githubClientConfigProperties;
        this.githubAPI = githubAPI;
    }

    public GithubUserDto getWhoAmI() {
        return githubAPI.getUser(getAccessToken());
    }

    public GithubRepoDtos getUserRepos(String login) {
        GithubRepoDtos githubRepoDtos = new GithubRepoDtos();

        int page = 1;
        List<GithubRepoDto> githubRepoDtoList;
        do {
            githubRepoDtoList = githubAPI.getUserRepos(getAccessToken(), login, page);
            githubRepoDtos.addGithubRepoDtos(githubRepoDtoList);
            page++;

        } while (githubRepoDtoList == null || !githubRepoDtoList.isEmpty());

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

    public Optional<GithubPullDtos> createPullRequest(String login, String repoName,
                                                      GithubPullRequestDto githubPullRequestDto) {
        try {
            GithubPullDto githubPullDto = githubAPI.createPullRequest(getAccessToken(), login, repoName, githubPullRequestDto);

            GithubPullDtos githubPullDtos = new GithubPullDtos();
            githubPullDtos.addGithubPullDto(githubPullDto);
            return Optional.of(githubPullDtos);

        } catch (FeignException e) {
            String errorMsg = String.format("Unable to create pull request found for repo=%s of user=%s", repoName, login);
            log.info(errorMsg);
            log.debug(String.format("%s : ex-msg %s", errorMsg, e.getMessage()));
        }
        return Optional.empty();
    }
}
