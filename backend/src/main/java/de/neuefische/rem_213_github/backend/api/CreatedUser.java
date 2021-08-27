package de.neuefische.rem_213_github.backend.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatedUser {

    private String username;
    private String password;
    private String role;
    private String avatarUrl;

}
