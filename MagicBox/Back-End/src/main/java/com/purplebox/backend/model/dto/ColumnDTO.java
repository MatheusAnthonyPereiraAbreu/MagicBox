package com.purplebox.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.purplebox.backend.mapper.ColumnDeserializer;
import com.purplebox.backend.model.dto.select.Select;
import com.purplebox.backend.model.dto.table.Table;

@JsonDeserialize(using = ColumnDeserializer.class)
public record ColumnDTO(
        Table table,
        Select field,
        String alias
) {

}
