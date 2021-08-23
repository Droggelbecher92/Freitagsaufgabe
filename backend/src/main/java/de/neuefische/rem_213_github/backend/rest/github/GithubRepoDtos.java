package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class GithubRepoDtos {

    List<GithubRepoDto> githubRepoDtos = new LinkedList<>();

    public void addGithubRepoDtos(List<GithubRepoDto> githubRepoDtos) {
        this.githubRepoDtos.addAll(new LinkedList<>(githubRepoDtos));
    }
}
