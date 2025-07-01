package com.magicbox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.magicbox.backend.mapper.AggregationDeserializer;
import com.magicbox.backend.model.dto.aggregation.Aggregation;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;

@JsonDeserialize(using = AggregationDeserializer.class)
public record AggregationDTO(
        Table table,
        Select field,
        Aggregation aggregation,
        String alias
) {

}