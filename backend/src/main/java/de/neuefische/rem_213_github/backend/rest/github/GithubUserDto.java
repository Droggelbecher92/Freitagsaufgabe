package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUserDto {

    private String name;
    private String login;
    private String avatar_url;
}
