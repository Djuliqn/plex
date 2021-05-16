package com.plex.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seasons")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "season_number")
    private Long seasonNumber;

    @OneToMany(
            mappedBy = "season",
            cascade = CascadeType.ALL)
    private Set<Episode> episodes;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "series_id")
    private Series series;

    public void addEpisode(Episode episode) {
        episodes.add(episode);
        episode.setSeason(this);
    }

    public void removeEpisode(Episode episode) {
        episodes.remove(episode);
        episode.setSeason(null);
    }

}