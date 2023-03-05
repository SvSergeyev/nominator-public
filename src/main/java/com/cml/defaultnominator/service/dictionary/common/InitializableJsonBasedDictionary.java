package com.cml.defaultnominator.service.dictionary.common;

import java.io.FileNotFoundException;

public interface InitializableJsonBasedDictionary {
    void setLocation(String jsonFileLocation);
    void parse() throws FileNotFoundException;
}
