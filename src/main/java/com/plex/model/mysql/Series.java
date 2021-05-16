package com.plex.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "series")
public class Series extends Media {

    @OneToMany(
            mappedBy = "series",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Season> seasons;

    public void addSeason(Season season) {
        seasons.add(season);
        season.setSeries(this);
    }

    public void removeSeason(Season season) {
        seasons.remove(season);
        season.setSeries(null);
    }
}