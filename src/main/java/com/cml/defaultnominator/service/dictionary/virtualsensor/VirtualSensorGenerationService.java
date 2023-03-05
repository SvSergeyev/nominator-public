package com.cml.defaultnominator.service.dictionary.virtualsensor;

import com.cml.defaultnominator.component.dictionary.virtualsensor.VirtualSensorDictionary;
import com.cml.defaultnominator.component.dictionary.virtualsensor.VirtualSensorDictionaryObject;
import com.cml.defaultnominator.dto.dictionary.virtualsensor.VirtualSensorDictionaryObjectOut;
import com.cml.defaultnominator.dto.dictionary.virtualsensor.VirtualSensorDictionaryOut;
import com.cml.defaultnominator.service.dictionary.common.CommonJsonBasedDictionaryService;
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
public class VirtualSensorGenerationService extends CommonJsonBasedDictionaryService<VirtualSensorDictionary> {

    @Autowired
    @Override
    public void setLocation(@Value("${path.dictionaries.virtualsensor}") String jsonFileLocation) {
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
                VirtualSensorDictionary dictionary = new Gson().fromJson(new FileReader(file), VirtualSensorDictionary.class);
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

    public VirtualSensorDictionaryOut getDictionary(String nameFromRequest) {
        String name = nameFromRequest.trim().toLowerCase();
        if (!dictionaries.containsKey(name)) {
            throw new NoSuchElementException("Dictionary " + name + " not found");
        }
        VirtualSensorDictionaryObject[] dictionary = dictionaries.get(name).getContent();
        List<VirtualSensorDictionaryObjectOut> dtoList = new ArrayList<>();
        int id = 1;
        for (VirtualSensorDictionaryObject pair : dictionary) {
            VirtualSensorDictionaryObjectOut objWithId = VirtualSensorDictionaryObjectOut.builder()
                    .abbreviation(pair.getAbbreviation())
                    .fullName(pair.getFull())
                    .id(id++)
                    .build();
            dtoList.add(objWithId);
        }

        VirtualSensorDictionaryOut out = new VirtualSensorDictionaryOut();
        out.setDictionary(dtoList.toArray(new VirtualSensorDictionaryObjectOut[0]));

        return out;
    }

}
