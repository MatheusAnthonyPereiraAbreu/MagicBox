package com.purplebox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.purplebox.backend.mapper.WhereDeserializer;
import com.purplebox.backend.model.dto.select.Select;
import com.purplebox.backend.model.dto.table.Table;
import com.purplebox.backend.model.dto.where.Operator;

@JsonDeserialize(using = WhereDeserializer.class)
public record WhereDTO(
        Table table,
        Select field,
        Operator operator,
        String value
) {
}
