package com.purplebox.backend.model.dto;

import java.util.Set;

import com.purplebox.backend.model.dto.table.Table;

public record AdHocDTO(
        Table table,
        Set<JoinDTO> join,
        Set<ColumnDTO> column,
        Set<WhereDTO> where,
        GroupByDTO groupBy
) {

}