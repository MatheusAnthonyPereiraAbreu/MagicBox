package com.magicbox.backend.model.entity;

import com.magicbox.backend.model.dto.Selectable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pais")
public class Pais implements Selectable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(length = 3, unique = true)
    private String codigo;

    // Relacionamentos reversos para rankings
    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    private Set<RankingAtualArtistasPaises> rankingsArtistas = new HashSet<>();

    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    private Set<RankingAtualMusicasPaises> rankingsMusicas = new HashSet<>();
}
