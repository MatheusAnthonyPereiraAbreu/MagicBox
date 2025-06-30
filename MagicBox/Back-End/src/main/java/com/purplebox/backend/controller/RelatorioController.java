package com.purplebox.backend.controller;

import com.purplebox.backend.model.dto.TopMusicaDTO;
import com.purplebox.backend.service.RelatorioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/top-musicas")
    public List<TopMusicaDTO> getTopMusicasPorPais() {
        return service.buscarTopMusicas();
    }
}
