package com.purplebox.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;

import com.purplebox.backend.model.dto.Selectable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ranking_atual_artistas_paises")
public class RankingAtualArtistasPaises implements Selectable {

    @EmbeddedId
    private RankingAtualArtistasPaisesId id;

    /** posição do artista no país */
    private Short posicaoRanking;

    private LocalDateTime dataUltimaAtualizacao;

    /* ===================== Relaciones ===================== */
    @MapsId("artistaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    @MapsId("paisId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;
}