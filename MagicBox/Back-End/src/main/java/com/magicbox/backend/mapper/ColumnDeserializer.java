package com.magicbox.backend.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicbox.backend.model.dto.ColumnDTO;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;

// Deserializador customizado para objetos ColumnDTO a partir de JSON.
public class ColumnDeserializer extends GenericDeserializer<ColumnDTO> {

    public ColumnDeserializer() {
        super(ColumnDTO.class);
    }

    /**
     * Realiza a desserialização de um objeto ColumnDTO a partir de um JSON.
     */
    @Override
    public ColumnDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        String alias = node.has("alias") ? node.get("alias").asText() : null;
        Select field = convertField(table, fieldStr);

        return new ColumnDTO(table, field, alias);
    }
}
