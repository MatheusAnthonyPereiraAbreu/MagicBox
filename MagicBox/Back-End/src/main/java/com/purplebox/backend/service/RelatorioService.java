package com.magicbox.backend.service;

import com.magicbox.backend.model.dto.TopMusicaDTO;
import com.magicbox.backend.repository.RelatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository repository;

    public RelatorioService(RelatorioRepository repository) {
        this.repository = repository;
    }

    public List<TopMusicaDTO> buscarTopMusicas() {
    return repository.buscarTopMusicas();
}

}
