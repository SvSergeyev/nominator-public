package com.cml.defaultnominator.dto.namedobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class CreateRequest extends NamedObjectRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRequest.class);

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String type;

    @Override
    public boolean containsNull() {
        if (this.getId() == null) {
            LOGGER.error("Id cannot be null");
            return false;
        }
        if (this.getType() == null) {
            LOGGER.error("Type cannot be null");
            return false;
        }
        return true;
    }
}
