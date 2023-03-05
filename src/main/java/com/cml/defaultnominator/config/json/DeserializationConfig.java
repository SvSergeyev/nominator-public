package com.cml.defaultnominator.config.json;

import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeserializationConfig {
    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(NamedObjectRequest.class, new NamedObjectDeserializer());
        return new ObjectMapper().registerModule(module);
    }
}
