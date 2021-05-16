package com.plex.model.mysql;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private Double rating;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "duration_milliseconds")
    private Long durationMilliseconds;

    @Column(name = "imdb_id")
    private String imdbId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "media_director",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id"))
    private Set<Director> directors;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "media_writer",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private Set<Writer> writers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "media_star",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "star_id"))
    private Set<Star> stars;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "media_genre",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;
}
