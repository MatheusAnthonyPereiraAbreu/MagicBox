package com.magicbox.backend.model.entity;

import com.magicbox.backend.model.dto.Selectable;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "artista_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistaTag implements Selectable {

    @EmbeddedId
    private ArtistaTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistaId")
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
