package com.magicbox.backend.model.dto;

public record TopMusicaDTO(
    String nomeArtista,
    String tagPrincipal,
    String nomeMusica,
    String nomeAlbum,
    String nomePais,
    Short posicaoRanking
) {}
