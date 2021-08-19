package de.neuefische.rem_213_github.backend.service;

import de.neuefische.rem_213_github.backend.config.UserServiceConfigProperties;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
@Getter
@Setter
public class UserService {

//    @Value("${rem213.user-service.avatar-url}")
//    private String avatarUrl;

    private UserRepository userRepository;
    private UserServiceConfigProperties userServiceConfigProperties;

    @Autowired
    public UserService(UserRepository userRepository, UserServiceConfigProperties userServiceConfigProperties) {
        this.userRepository = userRepository;
        this.userServiceConfigProperties = userServiceConfigProperties;
    }

    public UserEntity create(String name) {
        if (!hasText(name)) {
            throw new IllegalArgumentException("Name must not be blank");
        }

        Optional<UserEntity> existingUser = find(name);
        if (existingUser.isPresent()) {
            throw new EntityExistsException(String.format(
                    "User with name=%s already exists", name));
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setAvatarUrl(userServiceConfigProperties.getAvatarUrl());

        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> find(String name) {
        return userRepository.findByName(name);
    }

    public Optional<UserEntity> delete(String name) {
        Optional<UserEntity> userEntityOptional = find(name);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userRepository.delete(userEntity);
        }
        return userEntityOptional;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
}
