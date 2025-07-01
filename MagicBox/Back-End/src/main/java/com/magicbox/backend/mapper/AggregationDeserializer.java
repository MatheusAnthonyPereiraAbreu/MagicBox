package com.magicbox.backend.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magicbox.backend.model.dto.AggregationDTO;
import com.magicbox.backend.model.dto.aggregation.Aggregation;
import com.magicbox.backend.model.dto.select.Select;
import com.magicbox.backend.model.dto.table.Table;

// Deserializador customizado para objetos AggregationDTO a partir de JSON.
public class AggregationDeserializer extends GenericDeserializer<AggregationDTO> {

    public AggregationDeserializer() {
        super(AggregationDTO.class);
    }

    /**
     * Realiza a desserialização de um objeto AggregationDTO a partir de um JSON.
     */
    @Override
    public AggregationDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        String alias = node.has("alias") ? node.get("alias").asText() : null;
        Select field = convertField(table, fieldStr);
        Aggregation aggregation = Aggregation.valueOf(node.get("aggregation").asText());

        return new AggregationDTO(table, field, aggregation, alias);
    }


}
