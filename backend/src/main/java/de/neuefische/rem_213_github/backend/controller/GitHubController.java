package de.neuefische.rem_213_github.backend.controller;

import de.neuefische.rem_213_github.backend.api.GithubPullRequest;
import de.neuefische.rem_213_github.backend.api.GithubRepoPulls;
import de.neuefische.rem_213_github.backend.api.GithubUser;
import de.neuefische.rem_213_github.backend.api.GithubUserRepos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static de.neuefische.rem_213_github.backend.controller.GitHubControllerImpl.GIT_HUB_CONTROLLER;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = GIT_HUB_CONTROLLER, description = "Query Github API")
@Api(
        tags = GIT_HUB_CONTROLLER
)
@RequestMapping("/github")
public interface GitHubController {

    @Operation(summary = "Fetching configured user from GitHub.")
    @GetMapping(
            produces = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "User fetched from GitHub")
    })
    ResponseEntity<GithubUser> whoAmI();

    @Operation(summary = "Fetching all user repositories from GitHub.")
    @GetMapping(
            value = "/repos",
            produces = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "User repos from GitHub")
    })
    ResponseEntity<GithubUserRepos> getMyRepos();

    @Operation(summary = "Fetching all user repositories from GitHub by given Name.")
    @GetMapping(
            value = "/repos/{userName}",
            produces = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "User repos from GitHub")
    })
    ResponseEntity<GithubUserRepos> getUserRepos(@PathVariable("userName") String gitHubUserName);

    @Operation(summary = "Fetching a pull request from the users repo by name from GitHub.")
    @GetMapping(
            value = "/pulls/{repoName}",
            produces = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Pull request from GitHub"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Pull request not found at GitHub")
    })
    ResponseEntity<GithubRepoPulls> getMyPulls(@PathVariable("repoName") String repo);

    @Operation(summary = "Create a pull request for a repo by name at GitHub.")
    @PostMapping(
            value = "/pulls/{repoName}",
            produces = APPLICATION_JSON_VALUE
    )
    ResponseEntity<GithubRepoPulls> createPullRequest(@PathVariable("repoName") String repo,
                                                      GithubPullRequest githubPullRequest);


}
