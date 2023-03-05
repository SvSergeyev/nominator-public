package com.cml.defaultnominator.api.v2.namedobject;

import com.cml.defaultnominator.dto.namedobject.NamedObjectRequest;
import com.cml.defaultnominator.dto.namedobject.SuggestRequest;
import org.springframework.http.ResponseEntity;

public interface NamedObjectCrudInterface {

    ResponseEntity<?> getSuggestedName(NamedObjectRequest request);

    ResponseEntity<?> create(NamedObjectRequest request);

    ResponseEntity<?> delete(int objectId, String type);

    ResponseEntity<?> deleteTable();
}
