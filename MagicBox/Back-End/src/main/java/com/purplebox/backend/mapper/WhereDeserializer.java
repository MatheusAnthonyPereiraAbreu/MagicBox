package com.purplebox.backend.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purplebox.backend.model.dto.WhereDTO;
import com.purplebox.backend.model.dto.select.Select;
import com.purplebox.backend.model.dto.table.Table;
import com.purplebox.backend.model.dto.where.Operator;

public class WhereDeserializer extends GenericDeserializer<WhereDTO> {

    public WhereDeserializer() {

        super(WhereDTO.class);
    }

    @Override
    public WhereDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        Operator operator = Operator.getOperator(node.get("operator").asText());
        String value = node.get("value").asText();
        Select field = convertField(table, fieldStr);

        return new WhereDTO(table, field, operator, value);
    }
}
