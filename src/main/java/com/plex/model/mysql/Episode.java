package com.plex.model.mysql;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "episodes")
public class Episode extends Media {

    @Column(name = "episode_number")
    private Integer episodeNumber;

    private String audio;

    @Column(name = "subtitle_lang")
    private String subtitleLang;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "season_id")
    private Season season;
}