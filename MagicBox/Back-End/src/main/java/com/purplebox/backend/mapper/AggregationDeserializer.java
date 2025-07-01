package com.purplebox.backend.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purplebox.backend.model.dto.AggregationDTO;
import com.purplebox.backend.model.dto.aggregation.Aggregation;
import com.purplebox.backend.model.dto.select.Select;
import com.purplebox.backend.model.dto.table.Table;

public class AggregationDeserializer extends GenericDeserializer<AggregationDTO> {

    public AggregationDeserializer() {

        super(AggregationDTO.class);
    }

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
