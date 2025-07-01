package com.magicbox.backend.controller;

import com.magicbox.backend.model.dto.AdHocDTO;
import com.magicbox.backend.model.dto.TopMusicaDTO;
import com.magicbox.backend.service.RelatorioService;
import com.magicbox.backend.service.AdHocService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final AdHocService adHocService;

    public RelatorioController(RelatorioService relatorioService, AdHocService adHocService) {
        this.relatorioService = relatorioService;
        this.adHocService = adHocService;
    }

    @GetMapping("/top-musicas")
    public List<TopMusicaDTO> getTopMusicasPorPais() {
        return relatorioService.buscarTopMusicas();
    }

    @PostMapping("/ad-hoc")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object gerarRelatorio(@RequestBody AdHocDTO request) {
        return adHocService.gerarRelatorio(request);
    }
}
