package de.neuefische.rem_213_github.backend;

import de.neuefische.rem_213_github.backend.config.JpaConfig;
import de.neuefische.rem_213_github.backend.rest.github.GithubAPI;
import de.neuefische.rem_213_github.backend.service.UserService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AliasFor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.mockito.Mockito.mock;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"de.neuefische.rem_213_github.backend"})
@Import({JpaConfig.class})
@TestConfiguration
public class SpringTestContextConfiguration {

    public static final String MOCKED_SERVICES_PROFILE = "mockedUserService";

    @Primary
    @Bean(name = "dataSource", destroyMethod = "shutdown")
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Primary
    @Bean
    @Profile(MOCKED_SERVICES_PROFILE)
    public UserService userService() {
        return mock(UserService.class);
    }

    @Primary
    @Bean(name = "githubAPIMock")
    @AliasFor("githubAPI")
    public GithubAPI getGithubAPI() {
        return mock(GithubAPI.class);
    }
}
