package com.magicbox.backend.model.dto;

import com.magicbox.backend.model.dto.table.Table;

import jakarta.persistence.criteria.JoinType;

public record JoinDTO(
        Table from,
        Table to,
        JoinType type
) {


}