package kr.re.ImportTest2.component.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Converter
public class CategoryResultConverter implements AttributeConverter<CategoryResultTable, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CategoryResultTable attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryResultTable convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, CategoryResultTable.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
