package de.neuefische.rem_213_github.backend;

import de.neuefische.rem_213_github.backend.config.JpaConfig;
import de.neuefische.rem_213_github.backend.service.UserService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.mockito.Mockito.mock;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"de.neuefische.rem_213_github.backend"})
@Import({JpaConfig.class})
@TestConfiguration
public class SpringTestConfig {

    public static final String MOCKED_USER_SERVICE_PROFILE = "mockedUserService";

    @Primary
    @Bean(name = "dataSource", destroyMethod = "shutdown")
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Primary
    @Bean
    @Profile(MOCKED_USER_SERVICE_PROFILE)
    public UserService userService() {
        return mock(UserService.class);
    }
}
