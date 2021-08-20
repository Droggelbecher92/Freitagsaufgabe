package de.neuefische.rem_213_github.backend.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GithubRepo {

    private String id;
    private String repoName;
}
