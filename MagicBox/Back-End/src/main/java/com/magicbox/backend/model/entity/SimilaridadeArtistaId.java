package com.magicbox.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimilaridadeArtistaId implements Serializable {
    @Column(name = "artista_base_id")
    private Integer artistaBaseId;

    @Column(name = "artista_similar_id")
    private Integer artistaSimilarId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilaridadeArtistaId that = (SimilaridadeArtistaId) o;
        return Objects.equals(artistaBaseId, that.artistaBaseId) &&
               Objects.equals(artistaSimilarId, that.artistaSimilarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistaBaseId, artistaSimilarId);
    }
}
