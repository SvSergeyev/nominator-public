package com.cml.defaultnominator.service.dictionary.common;

import com.cml.defaultnominator.component.dictionary.common.CommonDictionary;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CommonJsonBasedDictionaryService<T extends CommonDictionary> implements InitializableJsonBasedDictionary {
    protected String location;
    protected Map<String, T> dictionaries = new HashMap<>();
}
