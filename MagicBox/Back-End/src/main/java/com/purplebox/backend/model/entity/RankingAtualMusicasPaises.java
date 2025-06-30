package com.purplebox.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ranking_atual_musicas_paises")
public class RankingAtualMusicasPaises {

    @EmbeddedId
    private RankingAtualMusicasPaisesId id;

    private Short posicaoRanking;

    private LocalDateTime dataUltimaAtualizacao;

    @MapsId("musicaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musica_id", nullable = false)
    private Musica musica;

    @MapsId("paisId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;
}
