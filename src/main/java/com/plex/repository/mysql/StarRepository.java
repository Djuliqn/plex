package com.plex.repository.mysql;

import com.plex.model.mysql.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {

}