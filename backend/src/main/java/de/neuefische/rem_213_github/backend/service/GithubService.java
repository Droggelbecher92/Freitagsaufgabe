package de.neuefische.rem_213_github.backend.service;

import de.neuefische.rem_213_github.backend.api.GithubPullRequest;
import de.neuefische.rem_213_github.backend.rest.github.GithubClient;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullRequestDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GithubService {

    private final GithubClient githubClient;

    @Autowired
    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public GithubUserDto getWhoAmI() {
        return githubClient.getWhoAmI();
    }

    public GithubRepoDtos getUserRepos(GithubUserDto githubUserDto) {
        return githubClient.getUserRepos(githubUserDto.getLogin());
    }

    public Optional<GithubPullDtos> getPulls(GithubUserDto githubUserDto, String repoName) {
        return githubClient.getRepoPulls(githubUserDto.getLogin(), repoName);
    }

    public Optional<GithubPullDtos> create(GithubUserDto githubUserDto, String repoName,
                                           GithubPullRequest githubPullRequest) {
        GithubPullRequestDto githubPullRequestDto = GithubPullRequestDto.builder()
                .repo(repoName)
                .owner(githubUserDto.getLogin())
                .head(githubPullRequest.getHead())
                .base(githubPullRequest.getBase())
                .title(githubPullRequest.getTitle())
                .build();

        return githubClient.createPullRequest(githubUserDto.getLogin(), repoName, githubPullRequestDto);
    }
}
