package com.plex.repository.mysql;

import com.plex.model.mysql.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {

}
