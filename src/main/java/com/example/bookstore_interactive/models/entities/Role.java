package com.example.bookstore_interactive.models.entities;

import jakarta.persistence.*;
import com.example.bookstore_interactive.models.enums.UserRoles;
import lombok.Setter;

@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private UserRoles name;

    public Role(UserRoles name) {
        this.name = name;
    }

    public Role() {

    }

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    public UserRoles getName() {
        return name;
    }

}