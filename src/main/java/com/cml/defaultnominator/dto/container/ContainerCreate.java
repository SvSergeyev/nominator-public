package com.cml.defaultnominator.dto.container;

import com.cml.defaultnominator.dto.namedobject.CreateRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
public class ContainerCreate extends CreateRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerCreate.class);

    @JsonProperty("divisionCode")
    String divisionCode;

    @JsonProperty("productIndex")
    String productIndex;

    @JsonProperty("analysisType")
    String analysisType;

    @Override
    public boolean containsNull() {
        super.containsNull();
        if (this.getDivisionCode() == null) {
            LOGGER.error("Division code cannot be null");
            return true;
        }
        if (this.getProductIndex() == null) {
            LOGGER.error("Product index cannot be null");
            return true;
        }
        if (this.getAnalysisType() == null) {
            LOGGER.error("Analysis type cannot be null");
            return true;
        }
        return false;
    }
}
