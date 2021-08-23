package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.GithubPullRequest;
import de.neuefische.rem_213_github.backend.api.GithubRepoPulls;
import de.neuefische.rem_213_github.backend.api.GithubUser;
import de.neuefische.rem_213_github.backend.api.GithubUserRepos;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubUserDto;
import de.neuefische.rem_213_github.backend.service.GithubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin
@Slf4j
public class GitHubControllerImpl extends GitHubControllerMapper implements GitHubController {

    public static final String GIT_HUB_CONTROLLER = "Github";

    private final GithubService githubService;

    @Autowired
    public GitHubControllerImpl(GithubService githubService) {
        this.githubService = githubService;
    }

    public ResponseEntity<GithubUser> whoAmI() {
        GithubUserDto githubUserDto = githubService.getWhoAmI();
        GithubUser githubUser = map(githubUserDto);

        log.info("Who am I user fetched {}", githubUser);
        return ok(githubUser);
    }

    public ResponseEntity<GithubUserRepos> getMyRepos() {
        GithubUserDto githubUserDto = githubService.getWhoAmI();
        return getGithubUserRepos(githubUserDto);
    }

    public ResponseEntity<GithubUserRepos> getUserRepos(String gitHubUserName) {
        GithubUserDto githubUserDto = new GithubUserDto();
        githubUserDto.setLogin(gitHubUserName);
        return getGithubUserRepos(githubUserDto);
    }

    private ResponseEntity<GithubUserRepos> getGithubUserRepos(GithubUserDto githubUserDto) {
        GithubRepoDtos githubRepoDtos = githubService.getUserRepos(githubUserDto);
        GithubUserRepos githubUserRepos = map(githubRepoDtos);

        GithubUser githubUser = map(githubUserDto);
        githubUserRepos.setGithubUser(githubUser);

        log.info("Repos fetched repos {}", githubUserRepos);
        return ok(githubUserRepos);
    }

    public ResponseEntity<GithubRepoPulls> getMyPulls(String repo) {
        GithubUserDto githubUserDto = githubService.getWhoAmI();

        Optional<GithubPullDtos> githubPullDtosOpt = githubService.getPulls(githubUserDto, repo);
        if (githubPullDtosOpt.isEmpty()) {
            return notFound().build();
        }

        GithubPullDtos githubPullDtos = githubPullDtosOpt.get();
        GithubRepoPulls githubRepoPulls = map(githubPullDtos);

        GithubUser githubUser = map(githubUserDto);
        githubRepoPulls.setGithubUser(githubUser);

        log.info("Repos fetched repos {}", githubRepoPulls);
        return ok(githubRepoPulls);
    }

    @Override
    public ResponseEntity<GithubRepoPulls> createPullRequest(String repo, @RequestBody GithubPullRequest githubPullRequest) {
        GithubUserDto githubUserDto = new GithubUserDto();
        githubUserDto.setLogin(githubPullRequest.getOwner());

        Optional<GithubPullDtos> githubPullDtosOpt = githubService.create(githubUserDto, repo, githubPullRequest);
        if (githubPullDtosOpt.isEmpty()) {
            return notFound().build();
        }

        GithubPullDtos githubPullDtos = githubPullDtosOpt.get();
        GithubRepoPulls githubRepoPulls = map(githubPullDtos);

        GithubPullDto pullRequestUser = githubPullDtos.getGithubPullDtos().iterator().next();
        GithubUser githubUser = map(pullRequestUser.getUser());
        githubRepoPulls.setGithubUser(githubUser);

        log.info("Pull request created for repo {}", githubRepoPulls);
        return ok(githubRepoPulls);
    }
}
