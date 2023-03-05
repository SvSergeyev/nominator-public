package com.cml.defaultnominator.component.dictionary.common;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class CommonDictionaryObject {

    @SerializedName("short")
    private String abbreviation;

    @SerializedName("full")
    private String full;

}
