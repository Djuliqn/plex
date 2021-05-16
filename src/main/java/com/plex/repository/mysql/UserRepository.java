package com.plex.repository.mysql;

import com.plex.model.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1 OR u.username = ?1")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
}
