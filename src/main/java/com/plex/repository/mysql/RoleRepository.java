package com.plex.repository.mysql;

import com.plex.model.mysql.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
