package de.neuefische.rem_213_github.backend.rest.github;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

import static de.neuefische.rem_213_github.backend.rest.github.GithubAPI.ACCESS_TOKEN_PARAM;

@Headers(
        "Authorization: Bearer {" + ACCESS_TOKEN_PARAM + "}"
)
public interface GithubAPI {

    String ACCESS_TOKEN_PARAM = "access-token";

    @RequestLine("GET /user")
    GithubUserDto getUser(@Param(ACCESS_TOKEN_PARAM) String accessToken);

    @RequestLine("GET /users/{owner}/repos")
    List<GithubRepoDto> getUserRepos(@Param(ACCESS_TOKEN_PARAM) String accessToken, @Param("owner") String userName);

    @RequestLine("GET /repos/{owner}/{repo}/pulls")
    List<GithubPullDto> getRepoPulls(@Param(ACCESS_TOKEN_PARAM) String accessToken, @Param("owner") String userName, @Param("repo") String repoName);
}
