package com.purplebox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.purplebox.backend.mapper.AggregationDeserializer;
import com.purplebox.backend.model.dto.aggregation.Aggregation;
import com.purplebox.backend.model.dto.select.Select;
import com.purplebox.backend.model.dto.table.Table;

@JsonDeserialize(using = AggregationDeserializer.class)
public record AggregationDTO(
        Table table,
        Select field,
        Aggregation aggregation,
        String alias
) {

}