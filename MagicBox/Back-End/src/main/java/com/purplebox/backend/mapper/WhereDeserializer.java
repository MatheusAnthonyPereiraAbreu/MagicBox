package com.magicbox.backend.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicbox.backend.model.dto.WhereDTO;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;
import com.magicbox.backend.model.dto.where.Operator;

// Deserializador customizado para objetos WhereDTO a partir de JSON.
public class WhereDeserializer extends GenericDeserializer<WhereDTO> {

    public WhereDeserializer() {
        super(WhereDTO.class);
    }

    /**
     * Realiza a desserialização de um objeto WhereDTO a partir de um JSON.
     */
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
