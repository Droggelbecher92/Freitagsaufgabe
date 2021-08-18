package de.neuefische.rem_213_github.backend.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rem_user")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntity that = (UserEntity) o;

        if (this.getId() != null) {
            return this.getId().equals(that.getId());
        }

        return this.getName() != null && this.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getId() == null ? getName().hashCode() : getId().hashCode();
    }
}
