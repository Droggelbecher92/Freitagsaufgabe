package de.neuefische.rem_213_github.backend.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GithubPull {

    String url;
    String title;
    String state;
}
