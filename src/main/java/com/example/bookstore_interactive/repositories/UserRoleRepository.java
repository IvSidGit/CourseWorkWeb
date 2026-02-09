package com.example.bookstore_interactive.repositories;


import com.example.bookstore_interactive.models.entities.Role;
import com.example.bookstore_interactive.models.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(UserRoles role);
}
