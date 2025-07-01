package com.magicbox.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.magicbox.backend.model.dto.Selectable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "artista")
public class Artista implements Selectable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    private Long ouvintesGlobais;

    private Long playcountGlobais;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> album;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Musica> musica;

    @ManyToMany
    @JoinTable(
            name = "artista_tag",
            joinColumns = @JoinColumn(name = "artista_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "similaridade_artista",
            joinColumns = @JoinColumn(name = "artista_base_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_similar_id")
    )
    private Set<Artista> similaridade_artista = new HashSet<>();

    // Relacionamentos reversos para rankings
    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY)
    private Set<RankingAtualArtistasPaises> rankingsArtistas = new HashSet<>();

    // Relacionamento reverso para ArtistaTag
    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY)
    private Set<ArtistaTag> artistaTags = new HashSet<>();
}

