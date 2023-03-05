package com.cml.defaultnominator.component.dictionary.domain;

import com.cml.defaultnominator.component.dictionary.common.CommonDictionary;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class DomainDictionary extends CommonDictionary {

    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private DomainDictionaryObject[] content;

}
