package com.cml.defaultnominator.dto.target;

import com.cml.defaultnominator.dto.dictionary.fn.CodeNamePair;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TargetOut extends NamedObjectRequest {

    @JsonProperty("target")
    TargetShort target;

    @JsonProperty("dict")
    List<CodeNamePair> dictionary;

    @JsonProperty("classifierEnabled")
    boolean classifierEnabled;

    @JsonProperty("classifierAvailable")
    boolean classifierAvailable;

}
