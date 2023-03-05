package com.cml.defaultnominator.dto.target;

import com.cml.defaultnominator.dto.namedobject.CreateRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class TargetCreate extends CreateRequest {

    @JsonProperty("productIndex")
    String productIndex;

    @JsonProperty("fn")
    Map<String, String> fn;

    @JsonProperty("name")
    String name;

    @Override
    public boolean containsNull() {
        return this.getProductIndex() == null
                || this.getType() == null
                || this.getId() == null;
    }
}
