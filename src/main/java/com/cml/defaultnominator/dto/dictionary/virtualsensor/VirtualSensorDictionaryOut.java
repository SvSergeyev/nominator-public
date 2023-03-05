package com.cml.defaultnominator.dto.dictionary.virtualsensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class VirtualSensorDictionaryOut {

    @JsonProperty("results")
    private VirtualSensorDictionaryObjectOut[] dictionary;

}
