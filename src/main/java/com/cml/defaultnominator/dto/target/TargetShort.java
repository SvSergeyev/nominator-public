package com.cml.defaultnominator.dto.target;

import com.cml.defaultnominator.dto.dictionary.fn.CodeNamePair;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TargetShort {

    @JsonProperty("productIndex")
    String productIndex;

    @JsonProperty("fn")
    Map<String, CodeNamePair> fn;

    @JsonProperty("revision")
    String revision;

    @JsonProperty("numEntity")
    String numEntity;

    @Override
    public String toString() {
        return "TargetShort{" +
                "productIndex='" + productIndex + '\'' +
                ", fn=" + fn +
                ", revision='" + revision + '\'' +
                ", numEntity='" + numEntity + '\'' +
                '}';
    }
}
