package com.cml.defaultnominator.api.v2.container;

import com.cml.defaultnominator.api.v2.namedobject.AbstractNamedObjectController;
import com.cml.defaultnominator.dto.container.ContainerOut;
import com.cml.defaultnominator.dto.namedobject.IdentityData;
import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.entity.internal.container.Container;
import com.cml.defaultnominator.service.container.ContainerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@Slf4j
@RequestMapping("${url.controller.prefix}/container")
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContainerController extends AbstractNamedObjectController<Container, ContainerService> {

    public ContainerController(ContainerService service) {
        super(service);
    }

    @Override
    @PostMapping("suggest")
    public ResponseEntity<?> getSuggestedName(@RequestBody NamedObjectRequest request) {
        try {
            SEMAPHORE.acquire();
            ContainerOut name = service.getSuggestedName(request);
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

    /**
     * Удаляет элемент из таблицы 'container'<br/>
     * Уникальным ключом для удаления объектов в таблице 'container' является значение в колонке 'id',
     * представляющее собой хэш-код комбинации значений колонок 'objectId' и 'type'
     * (подробнее см. {@linkplain ContainerService#generateIdByObjectIdAndType})
     *
     * @param objectId идентификатор объекта
     * @param type     тип объекта в соответствии с {@linkplain com.cml.defaultnominator.service.namedobject.NamedObjectTypes}
     * @return {@code ResponseEntity.HttpStatus(204)}
     */
    @Override
    @DeleteMapping("{type}/{objectId}")
    @Deprecated
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
}
