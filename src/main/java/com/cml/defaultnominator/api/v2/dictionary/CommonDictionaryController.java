package com.cml.defaultnominator.api.v2.dictionary;

import com.cml.defaultnominator.service.dictionary.domain.DomainGenerationService;
import com.cml.defaultnominator.service.dictionary.virtualsensor.VirtualSensorGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("${url.controller.prefix}/dic")
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true"
)
public class CommonDictionaryController {

    private final DomainGenerationService domainGenerationService;
    private final VirtualSensorGenerationService virtualSensorGenerationService;

    public CommonDictionaryController(DomainGenerationService dgs,
                                      VirtualSensorGenerationService vsgs) {
        this.domainGenerationService = dgs;
        this.virtualSensorGenerationService = vsgs;
    }

    @GetMapping(value = "domain", produces = "application/json")
    public ResponseEntity<?> getDomainNameGenerationDictionary(@RequestParam(value = "name") String name) {
        log.info("Received GET-request for a dictionary '{}'", name);
        try {
            return ResponseEntity.ok(domainGenerationService.getDictionary(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    @GetMapping(value = "virtualSensor", produces = "application/json")
    public ResponseEntity<?> getVirtualSensorNameGenerationDictionary(@RequestParam(value = "name") String name) {
        log.info("Received GET-request for a dictionary '{}'", name);
        try {
            return ResponseEntity.ok(virtualSensorGenerationService.getDictionary(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }
}
