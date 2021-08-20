package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepoDto {

    private String id;
    private String name;
    private GithubUserDto owner;
}
