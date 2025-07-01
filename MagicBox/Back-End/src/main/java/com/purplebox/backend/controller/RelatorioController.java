package com.magicbox.backend.controller;

import com.magicbox.backend.model.dto.AdHocDTO;
import com.magicbox.backend.model.dto.TopMusicaDTO;
import com.magicbox.backend.service.RelatorioService;
import com.magicbox.backend.service.AdHocService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador responsável pelos endpoints de relatórios da aplicação.
@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final AdHocService adHocService;

    public RelatorioController(RelatorioService relatorioService, AdHocService adHocService) {
        this.relatorioService = relatorioService;
        this.adHocService = adHocService;
    }

    /**
     * Retorna o ranking das músicas mais tocadas por país.
     */
    @GetMapping("/top-musicas")
    public List<TopMusicaDTO> getTopMusicasPorPais() {
        return relatorioService.buscarTopMusicas();
    }

    /**
     * Gera um relatório personalizado (ad-hoc) com base nos parâmetros enviados no corpo da requisição.
     */
    @PostMapping("/ad-hoc")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object gerarRelatorio(@RequestBody AdHocDTO request) {
        return adHocService.gerarRelatorio(request);
    }
}
