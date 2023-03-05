package com.cml.defaultnominator.dto.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@RequiredArgsConstructor
public class DomainDictionaryObjectOut {

    @JsonProperty("id")
    private final int id;

    @JsonProperty("text")
    private final String fullName;

    @JsonProperty("short")
    private final String abbreviation;
}
