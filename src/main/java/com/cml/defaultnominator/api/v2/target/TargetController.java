package com.cml.defaultnominator.api.v2.target;

import com.cml.defaultnominator.api.v2.namedobject.AbstractNamedObjectController;
import com.cml.defaultnominator.dto.dictionary.fn.FunctionalNumberDictionaryRequest;
import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.dto.target.TargetOut;
import com.cml.defaultnominator.entity.internal.target.Target;
import com.cml.defaultnominator.service.target.TargetService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("${url.controller.prefix}/target")
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TargetController extends AbstractNamedObjectController<Target, TargetService> {

    public TargetController(TargetService service) {
        super(service);
    }

    @Override
    @PostMapping("suggest")
    public ResponseEntity<?> getSuggestedName(@RequestBody NamedObjectRequest request) {
        try {
            SEMAPHORE.acquire();
            TargetOut name = service.getSuggestedName(request);
            SEMAPHORE.release();
            return new ResponseEntity<>(name, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            SEMAPHORE.release();
            log.error("Error:{}", Arrays.toString(e.getStackTrace()));
            return ResponseEntity.badRequest().body("Invalid request data");
        } catch (Exception e) {
            log.error("Suggest request failed: {}", Arrays.toString(e.getStackTrace()));
            SEMAPHORE.release();
        }
        SEMAPHORE.release();
        return ResponseEntity.internalServerError().build();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@RequestBody NamedObjectRequest nor) {
        return super.create(nor);
    }

    @Override
    @Deprecated
    @DeleteMapping("{type}/{objectId}")
    public ResponseEntity<?> delete(@PathVariable int objectId,
                                    @PathVariable String type) {
        return super.delete(objectId, type);
    }

    @DeleteMapping()
    public ResponseEntity<?> newDelete(@RequestBody IdentityData object) {
        return super.delete(object.getId(), object.getType());
    }

    @Override
    @DeleteMapping("all")
    public ResponseEntity<?> deleteTable() {
        return super.deleteTable();
    }

    @PutMapping()
    public ResponseEntity<?> updateAllTargetNames() {
        log.info("PUT-request was received to update the names of all targets");
        try {
            SEMAPHORE.acquire();
            service.updateAllTargetNames();
            SEMAPHORE.release();
            return ResponseEntity.ok().build();
        } catch (JpaSystemException jse) {
            log.error("Unable to access database connection: {}", Arrays.toString(jse.getStackTrace()));
        } catch (Exception e) {
            log.error("Internal exception while updating all targets name: {}", Arrays.toString(e.getStackTrace()));
        }
        SEMAPHORE.release();
        log.warn("Internal error while updating all targets. Number of available database connections:{}",
                SEMAPHORE.availablePermits());
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("{objectId}")
    public ResponseEntity<?> updateTargetName(@PathVariable int objectId) {
        log.info("PUT-request was received to update the name of target with objectId={}", objectId);
        try {
            SEMAPHORE.acquire();
            service.updateTargetName(objectId);
            SEMAPHORE.release();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Unable to access database connection: {}", Arrays.toString(e.getStackTrace()));
        }
        SEMAPHORE.release();
        log.warn("Internal error while updating target={}. Number of available database connections={}",
                objectId, SEMAPHORE.availablePermits());
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("dic")
    public ResponseEntity<TargetOut> getContent(@RequestBody(required = false) FunctionalNumberDictionaryRequest request) {
        return ResponseEntity.ok(service.getDictionaryWithActualEntityNumber(request));
    }
}
