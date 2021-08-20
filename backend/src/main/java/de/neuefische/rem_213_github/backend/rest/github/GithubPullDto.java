package de.neuefische.rem_213_github.backend.rest.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubPullDto {

    String url;
    String title;
    String state;
}
