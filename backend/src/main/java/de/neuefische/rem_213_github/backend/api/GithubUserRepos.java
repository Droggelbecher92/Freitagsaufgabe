package de.neuefische.rem_213_github.backend.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
public class GithubUserRepos {

    private GithubUser githubUser;
    private final List<GithubRepo> gitHubRepos = new LinkedList<>();

    public void add(GithubRepo githubRepo) {
        gitHubRepos.add(githubRepo);
    }

}
