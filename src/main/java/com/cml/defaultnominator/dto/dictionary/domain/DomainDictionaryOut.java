package com.cml.defaultnominator.dto.dictionary.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class DomainDictionaryOut {

    @JsonProperty("results")
    private DomainDictionaryObjectOut[] dictionary;

}
