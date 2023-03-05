package com.cml.defaultnominator.component.dictionary.functionalnumber;

import com.cml.defaultnominator.component.dictionary.common.CommonDictionary;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.Map;

@Getter
public class FunctionalNumberDictionary extends CommonDictionary {

    @SerializedName("name")
    String name;

    @SerializedName("content")
    Map<String, FunctionalNumberDictionary> content;

}
