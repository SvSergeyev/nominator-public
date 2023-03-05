package com.cml.defaultnominator.dto.container;

import com.cml.defaultnominator.dto.namedobject.SuggestRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ContainerSuggest extends SuggestRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerSuggest.class);

    @JsonProperty("uid")
    Integer uid;

    @Override
    public String toString() {
        return "ContainerSuggest{" +
                "type='" + this.getType() + '\'' +
                ", action='" + this.getAction() + '\'' +
                ", parent={" + this.getParent().toString() + "}" +
                ", uid=" + uid +
                '}';
    }

    @Override
    public boolean containsNull() {
        super.containsNull();
        if (this.getUid() == null) {
            LOGGER.warn("UID cannot be null");
            return true;
        }
        return false;
    }
}
