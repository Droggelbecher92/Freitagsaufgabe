package de.neuefische.rem_213_github.backend.model;

import de.neuefische.rem_213_github.backend.SpringBootTests;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UserEntityTest extends SpringBootTests {

    @Resource
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testCreateUserWithoutNameShouldThrow() {
        try {
            UserEntity userEntity = new UserEntity();
            userRepository.save(userEntity);
            fail("user without name must not be persisted");

        } catch (DataIntegrityViolationException ignore) {
            // expected
        }
    }

    @Test
    @Transactional
    public void testCreateUserName() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("foo");
        assertNull(userEntity.getId());

        UserEntity createdEntity = userRepository.save(userEntity);
        assertNotNull(createdEntity.getId());
        assertEquals(createdEntity, userEntity);

        Set<UserEntity> users = new HashSet<>();
        users.add(userEntity);
        users.add(createdEntity);
        assertEquals(1, users.size());
    }

    @Test
    @Transactional
    public void testUserContainsName() {
        // GIVEN
        UserEntity userEntity = new UserEntity();
        userEntity.setName("foo");
        UserEntity expectedUser = userRepository.save(userEntity);

        // WHEN
        Optional<UserEntity> actualUserOpt = userRepository.findByNameContains("o");
        assertTrue(actualUserOpt.isPresent());

        // THEN
        assertEquals(expectedUser, actualUserOpt.get());
    }
}
