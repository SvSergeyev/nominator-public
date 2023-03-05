package com.cml.defaultnominator.dto.container;

import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContainerOut extends NamedObjectRequest {

    @JsonProperty("divisionCode")
    String divisionCode;

    @JsonProperty("productIndex")
    String productIndex;

    @JsonProperty("analysisType")
    String analysisType;

    @JsonProperty("numEntity")
    String numEntity;

    @JsonProperty("type")
    String type;

    @JsonProperty("version")
    String version;

    @JsonProperty("classifierEnabled")
    boolean classifierEnabled;

    @JsonProperty("classifierAvailable")
    boolean classifierAvailable;
}
