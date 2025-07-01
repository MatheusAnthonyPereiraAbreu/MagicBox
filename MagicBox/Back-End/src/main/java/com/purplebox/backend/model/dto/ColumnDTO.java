package com.magicbox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.magicbox.backend.mapper.ColumnDeserializer;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;

@JsonDeserialize(using = ColumnDeserializer.class)
public record ColumnDTO(
        Table table,
        Select field,
        String alias
) {

}
