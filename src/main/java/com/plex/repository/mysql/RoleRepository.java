package com.plex.repository.mysql;

import com.plex.model.mysql.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAuthority(String authority);

    Set<Role> findByAuthorityIn(Set<String> authorities);
}
