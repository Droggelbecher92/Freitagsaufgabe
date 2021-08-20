package de.neuefische.rem_213_github.backend.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
public class GithubRepoPulls {

    private GithubUser githubUser;
    private final List<GithubPull> gitHubPulls = new LinkedList<>();

    public void add(GithubPull gitHubPull) {
        gitHubPulls.add(gitHubPull);
    }

}
