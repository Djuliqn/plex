package com.plex.model.sqlite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "metadata_items")
public class MetadataItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "metadata_type")
    private Integer metadataType;

    private String guid;

    private String title;

    private Double rating;

    private String summary;

    private Long duration;

    @Column(name = "tags_genre")
    private String tagsGenre;

    @Column(name = "tags_director")
    private String tagsDirector;

    @Column(name = "tags_writer")
    private String tagsWriter;

    @Column(name = "tags_star")
    private String tagsStar;

    @Column(name = "originally_available_at")
    private String originallyAvailableAt;

}
