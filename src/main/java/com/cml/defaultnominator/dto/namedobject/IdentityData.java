package com.cml.defaultnominator.dto.namedobject;

import com.cml.defaultnominator.service.namedobject.NamedObjectTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class IdentityData {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String type;

    public boolean isProject() {
        return type.equals(NamedObjectTypes.PRJ.getFullName());
    }

    public boolean isEmpty() {
        return id == null || type == null;
    }
}
