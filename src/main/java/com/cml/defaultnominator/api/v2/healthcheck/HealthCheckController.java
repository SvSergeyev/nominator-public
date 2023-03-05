package com.cml.defaultnominator.api.v2.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.controller.prefix}")
public class HealthCheckController {
    @Value("${application.version}")
    String version;

    @Value("${application.environment}")
    String env;

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(
                "I'm alive and waiting for you" +
                        "\nCurrent version " + version +
                        "\nCurrent run environment '" + env + "'");
    }
}
