package com.purplebox.backend.service;

import com.purplebox.backend.model.dto.TopMusicaDTO;
import com.purplebox.backend.repository.RelatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository repository;

    public RelatorioService(RelatorioRepository repository) {
        this.repository = repository;
    }

    public List<TopMusicaDTO> buscarTopMusicas() {
        return repository.buscarTopMusicasRaw().stream()
            .map(row -> new TopMusicaDTO(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4],
                row[5] != null ? ((Number) row[5]).shortValue() : null
            ))
            .toList();
    }
}
