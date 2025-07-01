package com.magicbox.backend.model.dto;

import java.util.Set;

public record GroupByDTO(
        Set<ColumnDTO> columnSet,
        AggregationDTO aggregation
) {

}