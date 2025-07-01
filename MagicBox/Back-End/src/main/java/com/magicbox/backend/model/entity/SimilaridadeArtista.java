package com.magicbox.backend.model.entity;

import com.magicbox.backend.model.dto.Selectable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "similaridade_artista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimilaridadeArtista implements Selectable {

    @EmbeddedId
    private SimilaridadeArtistaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistaBaseId")
    @JoinColumn(name = "artista_base_id")
    private Artista artistaBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistaSimilarId")
    @JoinColumn(name = "artista_similar_id")
    private Artista artistaSimilar;
}
