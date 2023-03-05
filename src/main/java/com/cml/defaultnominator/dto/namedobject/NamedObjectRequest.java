package com.cml.defaultnominator.dto.namedobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class NamedObjectRequest implements NonNullableData {

    @JsonProperty("action")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String action;

    @JsonProperty("parent")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    IdentityData parent;

    @Override
    public boolean containsNull() {
        return false;
    }
}
