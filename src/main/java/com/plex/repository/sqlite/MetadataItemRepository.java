package com.plex.repository.sqlite;

import com.plex.model.sqlite.MetadataItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface MetadataItemRepository extends JpaRepository<MetadataItem, Integer> {

    // metadataType with value 1 will get the data for all the movies
    @Query(value = "SELECT m FROM MetadataItem m WHERE m.metadataType = 1 AND m.duration IS NOT NULL")
    Set<MetadataItem> findAllMovie();

}