package com.purplebox.backend.model.dto;

import java.util.Set;

import com.purplebox.backend.model.dto.table.Table;

// DTO que representa a requisição para geração de relatórios ad-hoc (dinâmicos).
public record AdHocDTO(
        Table table,           // Tabela principal da consulta
        Set<JoinDTO> join,     // Relações (joins) com outras tabelas
        Set<ColumnDTO> column, // Colunas a serem retornadas
        Set<WhereDTO> where,   // Filtros da consulta
        GroupByDTO groupBy     // Parâmetros de agrupamento e agregação
) {

}