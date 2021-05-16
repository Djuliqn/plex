package com.plex.repository.mysql;

import com.plex.model.mysql.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MediaBaseRepository<T extends Media> extends JpaRepository<T, Long> {

}