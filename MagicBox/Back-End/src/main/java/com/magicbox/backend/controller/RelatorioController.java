package com.magicbox.backend.controller;

import com.magicbox.backend.model.dto.AdHocDTO;
import com.magicbox.backend.model.dto.TopMusicaDTO;
import com.magicbox.backend.service.RelatorioService;
import com.magicbox.backend.service.AdHocService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/ad-hoc", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Object gerarRelatorio(@RequestBody AdHocDTO request,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "50") int size) {
        Object result = adHocService.gerarRelatorio(request, page, size);
        // Se o resultado já for um Map com 'data' e 'total', apenas retorne
        if (result instanceof java.util.Map map && map.containsKey("data") && map.containsKey("total")) {
            return result;
        }
        // Caso contrário, retorne no formato esperado
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("data", result);
        response.put("total", (result instanceof java.util.Collection) ? ((java.util.Collection<?>) result).size() : 0);
        return response;
    }
}
