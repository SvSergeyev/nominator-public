package com.cml.defaultnominator.config.json;

import com.cml.defaultnominator.dto.container.ContainerCreate;
import com.cml.defaultnominator.dto.container.ContainerSuggest;
import com.cml.defaultnominator.dto.namedobject.ActionType;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.dto.target.TargetCreate;
import com.cml.defaultnominator.dto.target.TargetSuggest;
import com.cml.defaultnominator.service.namedobject.NamedObjectTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public final class NamedObjectDeserializer extends StdDeserializer<NamedObjectRequest> {

    public NamedObjectDeserializer() {
        this(null);
    }

    protected NamedObjectDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public NamedObjectRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        final JsonNode node = mapper.readTree(jsonParser);

        final ActionType action = ActionType.valueOf(node.get("action").asText());
        final NamedObjectTypes type = NamedObjectTypes.formatToNamedObjectType(node.get("type").asText());
        switch (Objects.requireNonNull(type)) {
            case TAR:
            case TGR:
                switch (Objects.requireNonNull(action)) {
                    case suggest:
                        return mapper.treeToValue(node, TargetSuggest.class);
                    case create:
                        return mapper.treeToValue(node, TargetCreate.class);
                    default:
                        log.warn("Unexpected action: {}", action);
                        throw new IllegalStateException("Unexpected action: " + action);
                }
            case SIM:
            case STP:
            case DMU:
            case WIKI:
            case FLD:
            case LCS:
            case CRT:
            case PRT:
            case ISS:
            case CSUB:
            case STB:
            case DOC:
            case PRJ:
                switch (Objects.requireNonNull(action)) {
                    case suggest:
                        return mapper.treeToValue(node, ContainerSuggest.class);
                    case create:
                        return mapper.treeToValue(node, ContainerCreate.class);
                    default:
                        log.warn("Unexpected action: {}", action);
                        throw new IllegalStateException("Unexpected action: " + action);
                }
            default:
                log.warn("Unexpected type of named object: {}", type);
                throw new IllegalStateException("Unexpected type of named object: " + type);
        }
    }
}
