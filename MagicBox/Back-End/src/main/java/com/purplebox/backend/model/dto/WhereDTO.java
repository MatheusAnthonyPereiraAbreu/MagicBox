package com.magicbox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.magicbox.backend.mapper.WhereDeserializer;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;
import com.magicbox.backend.model.dto.where.Operator;

@JsonDeserialize(using = WhereDeserializer.class)
public record WhereDTO(
        Table table,
        Select field,
        Operator operator,
        String value
) {
}
