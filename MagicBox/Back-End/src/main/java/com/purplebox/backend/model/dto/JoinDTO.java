package com.purplebox.backend.model.dto;

import com.purplebox.backend.model.dto.table.Table;

import jakarta.persistence.criteria.JoinType;

public record JoinDTO(
        Table from,
        Table to,
        JoinType type
) {


}