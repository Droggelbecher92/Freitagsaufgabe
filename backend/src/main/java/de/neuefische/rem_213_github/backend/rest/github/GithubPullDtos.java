package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class GithubPullDtos {

    List<GithubPullDto> githubPullDtos = new LinkedList<>();

    public void addGithubPullDto(GithubPullDto githubPullDto) {
        githubPullDtos.add(githubPullDto);
    }
}
