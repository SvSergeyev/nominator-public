package com.cml.defaultnominator.api.v2.namedobject;

import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.entity.internal.namedobject.AbstractNamedObjectEntity;
import com.cml.defaultnominator.service.namedobject.NamedObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

@Slf4j
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true"
)
public abstract class AbstractNamedObjectController<E extends AbstractNamedObjectEntity, S extends NamedObjectService<E>>
        implements NamedObjectCrudInterface {

    protected final Semaphore SEMAPHORE = new Semaphore(1);
    protected S service;

    @Value("${application.environment}")
    protected String env;

    public AbstractNamedObjectController(S service) {
        this.service = service;
    }

    /**
     * Формирует новое имя для объекта.
     * Если его родительская сущность отсутствует в БД - формирует новое имя.
     * В противном случае переносит наследуемую часть данных родительской сущности,
     * добавляя уникальные элементы в новое имя.
     *
     * @param request json, содержащий данные, необходимые для поиска родительской сущности и определения типа создаваемого объекта
     * @return ResponseEntity.status(201)
     */
    @Override
    public ResponseEntity<?> getSuggestedName(NamedObjectRequest request) {
        return null;
    }

    /**
     * Записывает в БД классификатора новый именованный объект-наследник {@linkplain AbstractNamedObjectEntity}
     *
     * @param request json, содержащий данные, необходимые для создания сущности в таблице БД
     * @return HttpStatus.200 {E extends AbstractNamedObjectEntity}
     */
    @Override
    public ResponseEntity<?> create(NamedObjectRequest request) {
        log.info("Create request received: {}", request);

        if (request.containsNull()) {
            log.error("Invalid create request");
            return ResponseEntity.badRequest().build();
        }

        try {
            SEMAPHORE.acquire();
            if (service.create(request)) {
                SEMAPHORE.release();
                return ResponseEntity.ok().build();
            } else {
                SEMAPHORE.release();
                return ResponseEntity.badRequest().body("Duplicate request");
            }
        } catch (JpaSystemException jse) {
            log.info("Database connection busy, retrying in 1 second");
            SEMAPHORE.release();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            create(request);
        } catch (Exception e) {
            SEMAPHORE.release();
            log.error("Internal error while creating object: {}", Arrays.toString(e.getStackTrace()));
            return ResponseEntity.internalServerError().build();
        }
        return null;
    }

    /**
     * Удаляет из соответствующей таблицы в собственной БД классификатора запись с указанным id и типом
     *
     * @param objectId non-null int
     * @param type     non-null string as {@linkplain com.cml.defaultnominator.service.namedobject.NamedObjectTypes}
     * @return ResponseEntity.noContent
     */
    @Override
    public ResponseEntity<?> delete(int objectId, String type) {
        log.info("Received request to delete an object with id={} and type={}", objectId, type);
        int id = service.generateIdByObjectIdAndType(objectId, type.toLowerCase());
        try {
            SEMAPHORE.acquire();
            if (!service.checkById(id)) {
                SEMAPHORE.release();
                log.info("Object with id={} and type={} not exist", objectId, type);
                return new ResponseEntity<>("Object not exist", HttpStatus.NO_CONTENT);
            }
            service.deleteById(id);
            SEMAPHORE.release();
            return new ResponseEntity<>("Object successfully removed", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Internal error while deleting named object: {}", Arrays.toString(e.getStackTrace()));
            SEMAPHORE.release();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Удаляет все записи из соответствующей таблицы в собственной БД классификатора
     *
     * @return ResponseEntity.noContent
     */
    @Override
    public ResponseEntity<?> deleteTable() {
        try {
            SEMAPHORE.acquire();
            if (!"dev".equalsIgnoreCase(env)) {
                log.warn("Deletion of all data from tables is possible only with application.environment=dev");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            String tableName = service.getClass().getSimpleName().toLowerCase().replace("service", "");
            log.info("Table '{}' will be cleared", tableName);
            service.deleteAll();
            SEMAPHORE.release();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            SEMAPHORE.release();
            log.error("Internal error: {}", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        log.warn("Internal error while dropping table");
        SEMAPHORE.release();
        return ResponseEntity.internalServerError().build();
    }
}
