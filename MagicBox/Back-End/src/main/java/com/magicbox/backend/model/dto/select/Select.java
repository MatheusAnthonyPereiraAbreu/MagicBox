package com.magicbox.backend.model.dto.select;

import com.magicbox.backend.model.dto.table.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public interface Select {

    String attribute();

    Class<?> type();

    default Object castValue(String raw) {
        if (raw == null)
            return null;

        Class<?> t = type();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        if (t == String.class)
            return raw;
        if (t == Integer.class)
            return Integer.parseInt(raw);
        if (t == Long.class)
            return Long.parseLong(raw);
        if (t == Boolean.class)
            return Boolean.parseBoolean(raw);
        if (t == Double.class)
            return Double.parseDouble(raw);
        if (Objects.equals(t, LocalDate.class))
            return LocalDate.parse(raw, formatter);

        throw new IllegalArgumentException("Não é possível converter " + raw + " para " + t.getSimpleName());
    }

    static List<Select> getFromTable(Table table) {
        return switch (table) {
            case ARTISTA -> List.of(ArtistaSelect.values());
            case ALBUM -> List.of(AlbumSelect.values());
            case MUSICA -> List.of(MusicaSelect.values());
            case PAIS -> List.of(PaisSelect.values());
            case TAG -> List.of(TagSelect.values());
            case USUARIO -> List.of(UsuarioSelect.values());
            case ARTISTA_TAG -> List.of(ArtistaTagSelect.values());
            case SIMILARIDADE_ARTISTA -> List.of(SimilaridadeArtistaSelect.values());
            case RANKING_ARTISTAS -> List.of(RankingAtualArtistasPaisesSelect.values());
            case RANKING_MUSICAS -> List.of(RankingAtualMusicasPaisesSelect.values());
        };
    }
}