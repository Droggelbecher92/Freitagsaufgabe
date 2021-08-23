package de.neuefische.rem_213_github.backend.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GithubPullRequest {

    private String title;
    private String owner;
    private String head;
    private String base;
}
