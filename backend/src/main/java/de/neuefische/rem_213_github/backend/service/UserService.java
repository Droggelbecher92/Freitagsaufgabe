package de.neuefische.rem_213_github.backend.service;

import de.neuefische.rem_213_github.backend.api.User;
import de.neuefische.rem_213_github.backend.config.UserServiceConfigProperties;
import de.neuefische.rem_213_github.backend.model.RepoEntity;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.repo.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.StringUtils.hasText;

@Service
@Getter
@Setter
public class UserService {

//    @Value("${rem213.user-service.avatar-url}")
//    private String avatarUrl;

    private UserRepository userRepository;
    private UserServiceConfigProperties userServiceConfigProperties;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserServiceConfigProperties userServiceConfigProperties, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userServiceConfigProperties = userServiceConfigProperties;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity create(UserEntity userEntity) {
        String name = userEntity.getName();
        if (!hasText(name)) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        checkUserNameExists(name);

        return userRepository.save(userEntity);
    }

    private void checkUserNameExists(String name) {
        Optional<UserEntity> existingUser = find(name);
        if (existingUser.isPresent()) {
            throw new EntityExistsException(String.format(
                    "User with name=%s already exists", name));
        }
    }

    public UserEntity update(Long id, UserEntity updateEntity) {
        Optional<UserEntity> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new EntityExistsException(String.format(
                    "User with id=%d not found, unable to update", id));
        }
        UserEntity existingUserEntity = existingUserOpt.get();

        String name = updateEntity.getName();
        if (name != null && !name.equals(existingUserEntity.getName())) {
            checkUserNameExists(name);
        }

        String avatarUrl = updateEntity.getAvatarUrl();
        if (avatarUrl != null) {
            existingUserEntity.setAvatarUrl(avatarUrl);
        }

        Set<RepoEntity> repos = updateEntity.getRepos();
        if (repos != null) {
            existingUserEntity.setRepos(repos);
        }
        return userRepository.save(existingUserEntity);
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

    public String generatePassword(){
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public Optional<UserEntity> updatePassword (UserEntity userEntity, String newPassword){
        if(newPassword==null || newPassword.length()<1){
            return Optional.empty();
        }
        String newHashedPassword = hashPassword(newPassword);
        userEntity.setPassword(newHashedPassword);
        Optional<UserEntity> dbUserOptional=userRepository.findByName(userEntity.getName());
        if(dbUserOptional.isEmpty()){
            return Optional.empty();
        }
        UserEntity dbUser = dbUserOptional.get();
        dbUser.setPassword(newHashedPassword);
        userRepository.save(dbUser);

        return Optional.of(dbUser);

    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Optional<UserEntity> resetPassword(String username, String newPassword) {
        Optional<UserEntity> userOpt = userRepository.findByName(username);
        if (userOpt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User wasn't found");
        } else if (userOpt.get().getRole().equals("ADMIN")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Admin cannot be resetet, please contact service!");
        }
        String newHashedPassword = hashPassword(newPassword);
        UserEntity userToChange = userOpt.get();
        userToChange.setPassword(newHashedPassword);
        userRepository.save(userToChange);
        return Optional.of(userToChange);
    }
}
