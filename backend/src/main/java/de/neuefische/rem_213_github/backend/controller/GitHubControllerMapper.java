package de.neuefische.rem_213_github.backend.controller;


import de.neuefische.rem_213_github.backend.api.GithubPull;
import de.neuefische.rem_213_github.backend.api.GithubRepo;
import de.neuefische.rem_213_github.backend.api.GithubRepoPulls;
import de.neuefische.rem_213_github.backend.api.GithubUser;
import de.neuefische.rem_213_github.backend.api.GithubUserRepos;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubUserDto;

import java.util.List;

abstract class GitHubControllerMapper {

    GithubRepoPulls map(GithubPullDtos githubPullDtos) {
        GithubRepoPulls githubRepoPulls = new GithubRepoPulls();

        List<GithubPullDto> githubPullDtoList = githubPullDtos.getGithubPullDtos();
        for (GithubPullDto githubPullDto : githubPullDtoList) {
            GithubPull githubPull = map(githubPullDto);
            githubRepoPulls.add(githubPull);
        }
        return githubRepoPulls;
    }

    private GithubPull map(GithubPullDto githubPullDto) {
        return GithubPull.builder()
                .title(githubPullDto.getTitle())
                .state(githubPullDto.getState())
                .url(githubPullDto.getUrl())
                .build();
    }

    GithubUserRepos map(GithubRepoDtos githubRepoDtos) {
        GithubUserRepos githubUserRepos = new GithubUserRepos();

        List<GithubRepoDto> githubRepoDtoList = githubRepoDtos.getGithubRepoDtos();
        for (GithubRepoDto githubRepoDto : githubRepoDtoList) {
            GithubRepo githubRepo = map(githubRepoDto);
            githubUserRepos.add(githubRepo);
        }
        return githubUserRepos;
    }

    private GithubRepo map(GithubRepoDto githubRepoDto) {
        return GithubRepo.builder()
                .id(githubRepoDto.getId())
                .repoName(githubRepoDto.getName())
                .build();
    }

    GithubUser map(GithubUserDto githubUserDto) {
        return GithubUser.builder()
                .userId(githubUserDto.getLogin())
                .name(githubUserDto.getName())
                .build();
    }
}
