package com.cml.defaultnominator.service.dictionary.domain;

import com.cml.defaultnominator.component.dictionary.domain.DomainDictionary;
import com.cml.defaultnominator.component.dictionary.domain.DomainDictionaryObject;
import com.cml.defaultnominator.dto.dictionary.domain.DomainDictionaryObjectOut;
import com.cml.defaultnominator.dto.dictionary.domain.DomainDictionaryOut;
import com.cml.defaultnominator.service.dictionary.common.CommonJsonBasedDictionaryService;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class DomainGenerationService extends CommonJsonBasedDictionaryService<DomainDictionary> {

    @Autowired
    @Override
    public void setLocation(@Value("${path.dictionaries.domain}") String jsonFileLocation) {
        this.location = jsonFileLocation;
    }

    @PostConstruct
    @Override
    public void parse() throws FileNotFoundException {
        File folder = new File(location);
        File[] files = folder.listFiles();

        if (files == null || files.length < 1) {
            throw new FileNotFoundException(String.format("Probably dictionary files are missing in %s", location));
        }

        for (File file : files) {
            try {
                String name = file.getName().split("\\.")[0].toLowerCase();
                DomainDictionary dictionary = new Gson().fromJson(new FileReader(file), DomainDictionary.class);
                if (dictionary.getContent() != null) {
                    log.info("Dictionary '{}' successfully parsed, find {} items", name, dictionary.getContent().length);
                    dictionaries.put(name, dictionary);
                } else {
                    throw new JsonSyntaxException("Dictionary '{}' was not processed");
                }
            } catch (FileNotFoundException e) {
                log.error("Probably dictionary files are missing in {}. Details: {}",
                        location, e.getStackTrace());
            } catch (JsonSyntaxException e) {
                log.error("Dictionary file {} has an incorrect structure and will not be processed. Probable cause: {}\n More details: {}",
                        location, e.getMessage(), e.getStackTrace());
            }
        }
    }

    public DomainDictionaryOut getDictionary(String nameFromRequest) {
        String name = nameFromRequest.trim().toLowerCase();
        if (!dictionaries.containsKey(name)) {
            throw new NoSuchElementException("Dictionary " + name + " not found");
        }
        DomainDictionaryObject[] dictionary = dictionaries.get(name).getContent();
        List<DomainDictionaryObjectOut> dtoList = new ArrayList<>();
        int id = 1;
        for (DomainDictionaryObject pair : dictionary) {
            DomainDictionaryObjectOut objWithId = DomainDictionaryObjectOut.builder()
                    .abbreviation(pair.getAbbreviation())
                    .fullName(pair.getFull())
                    .id(id++)
                    .build();
            dtoList.add(objWithId);
        }

        DomainDictionaryOut out = new DomainDictionaryOut();
        out.setDictionary(dtoList.toArray(new DomainDictionaryObjectOut[0]));

        return out;
    }

}
