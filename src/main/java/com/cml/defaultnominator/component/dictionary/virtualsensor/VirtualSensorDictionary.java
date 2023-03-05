package com.cml.defaultnominator.component.dictionary.virtualsensor;

import com.cml.defaultnominator.component.dictionary.common.CommonDictionary;
import com.cml.defaultnominator.component.dictionary.domain.DomainDictionaryObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class VirtualSensorDictionary extends CommonDictionary {

    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private VirtualSensorDictionaryObject[] content;

}
