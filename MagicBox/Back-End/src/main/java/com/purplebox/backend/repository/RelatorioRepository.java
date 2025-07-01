package com.magicbox.backend.repository;

import com.magicbox.backend.model.dto.TopMusicaDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelatorioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TopMusicaDTO> buscarTopMusicas() {
        String sql = """
            WITH primeira_tag_por_artista AS (
                SELECT
                    at.artista_id,
                    t.nome AS nome_tag,
                    ROW_NUMBER() OVER (PARTITION BY at.artista_id ORDER BY t.id ASC) AS rn
                FROM artista_tag at
                JOIN tag t ON t.id = at.tag_id
            ),
            top5_musicas_por_pais AS (
                SELECT *
                FROM ranking_atual_musicas_paises
                WHERE posicao_ranking <= 5
            )
            SELECT
                a.nome AS nome_artista,
                pt.nome_tag AS tag_principal,
                m.nome AS nome_musica,
                al.nome AS nome_album,
                p.nome AS nome_pais,
                rmp.posicao_ranking
            FROM top5_musicas_por_pais rmp
            JOIN musica m ON m.id = rmp.musica_id
            JOIN artista a ON a.id = m.artista_id
            LEFT JOIN album al ON al.id = m.album_id
            JOIN pais p ON p.id = rmp.pais_id
            LEFT JOIN primeira_tag_por_artista pt ON pt.artista_id = a.id AND pt.rn = 1
            ORDER BY p.nome, rmp.posicao_ranking
        """;

        return entityManager.createNativeQuery(sql)
            .getResultList()
            .stream()
            .map(row -> {
                Object[] cols = (Object[]) row;
                return new TopMusicaDTO(
                    (String) cols[0],
                    (String) cols[1],
                    (String) cols[2],
                    (String) cols[3],
                    (String) cols[4],
                    cols[5] != null ? ((Number) cols[5]).shortValue() : null
                );
            })
            .toList();
    }
}
