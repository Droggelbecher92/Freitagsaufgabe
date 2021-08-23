package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GithubPullRequestDto {

    private String title;
    private String owner;
    private String repo;
    private String head;
    private String base;
}
