package com.cml.defaultnominator.dto.namedobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@ToString
@FieldDefaults(
        level = AccessLevel.PRIVATE
)
public abstract class SuggestRequest extends NamedObjectRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestRequest.class);

    @JsonProperty("type")
    String type;

    @Override
    public boolean containsNull() {
        if (this.getType() == null) {
            LOGGER.error("Type cannot be null");
            return true;
        }
        return false;
    }
}
