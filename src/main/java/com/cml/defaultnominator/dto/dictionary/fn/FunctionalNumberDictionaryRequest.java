package com.cml.defaultnominator.dto.dictionary.fn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class FunctionalNumberDictionaryRequest {

    @JsonProperty("project")
    private String project;

    @JsonProperty("fields")
    Map<String, String> fields;

    public String[] convertDictionaryLevelsToArray() {
        List<String> out = new ArrayList<>();
        if (fields != null && !fields.isEmpty()) {
            out.addAll(fields.values());
        }
        return out.toArray(String[]::new);
    }
}
