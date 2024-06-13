package kr.re.ImportTest2.component.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;

import java.io.IOException;
import java.util.List;

@Converter
public class FlowResultTableListConverter implements AttributeConverter<List<FlowResultTable>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FlowResultTable> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Conversion error", e);
        }
    }

    @Override
    public List<FlowResultTable> convertToEntityAttribute(String dbData) {
        try {
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return mapper.readValue(dbData, new TypeReference<List<FlowResultTable>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Conversion error", e);
        }
    }
}