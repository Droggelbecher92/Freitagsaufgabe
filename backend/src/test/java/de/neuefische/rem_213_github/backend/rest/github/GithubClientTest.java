package de.neuefische.rem_213_github.backend.rest.github;

import de.neuefische.rem_213_github.backend.SpringBootTests;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GithubClientTest extends SpringBootTests {

    @Resource
    private GithubClient githubClient;

    @Resource(name = "githubAPIMock")
    private GithubAPI githubAPIMock; // this service is a mocked bean

    @Test
    public void getUserReposTest() {
        // GIVEN
        List<GithubRepoDto> githubRepoDtoList = new LinkedList<>();
        GithubRepoDto githubRepoDto = new GithubRepoDto();
        githubRepoDto.setId("1");
        githubRepoDtoList.add(githubRepoDto);

        // WHEN
        when(githubAPIMock.getUserRepos(any(), anyString(), eq(1)))
                .thenReturn(githubRepoDtoList)
                .thenReturn(Collections.emptyList());

        // THEN
        GithubRepoDtos githubRepoDtos = githubClient.getUserRepos("foo");

        assertEquals(1, githubRepoDtos.getGithubRepoDtos().size());
        verify(githubAPIMock, times(2)).getUserRepos(any(), anyString(), anyInt());
    }
}
