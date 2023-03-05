package com.cml.defaultnominator.service.dictionary.functionalnumber;

import com.cml.defaultnominator.component.dictionary.functionalnumber.FunctionalNumberDictionary;
import com.cml.defaultnominator.dto.dictionary.fn.CodeNamePair;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FunctionalNumberDictionaryService extends CommonJsonBasedDictionaryService<FunctionalNumberDictionary> {

    @Autowired
    @Override
    public void setLocation(@Value("${path.dictionaries.fn}") String jsonFileLocation) {
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
                FunctionalNumberDictionary dictionary = new Gson().fromJson(new FileReader(file), FunctionalNumberDictionary.class);
                if (dictionary.getContent() != null) {
                    log.info("Dictionary '{}' successfully parsed, find {} items", name, dictionary.getContent().size());
                    dictionaries.put(name, dictionary);
                } else {
                    throw new JsonSyntaxException("Dictionary '{}' was not processed");
                }
            } catch (FileNotFoundException e) {
                log.error("Probably dictionary files are missing in {}. Details: {}",
                        location, String.format(System.lineSeparator(), List.of(e.getStackTrace())));
            } catch (JsonSyntaxException e) {
                log.error("Dictionary file {} has an incorrect structure and will not be processed. Probable cause: {}\n More details: {}",
                        location, e.getMessage(), e.getStackTrace());
            }
        }
    }

    private List<CodeNamePair> getPairs(Map<String, FunctionalNumberDictionary> level) {
        List<CodeNamePair> out = new ArrayList<>();

        for (Map.Entry<String, FunctionalNumberDictionary> entry : level.entrySet()) {
            out.add(CodeNamePair.builder()
                    .code(entry.getKey())
                    .name(entry.getValue().getName())
                    .build());
        }
        return out;
    }


    public List<CodeNamePair> getLevel() {
        return dictionaries.entrySet().stream()
                .map(dictionary -> CodeNamePair.builder()
                        .code(dictionary.getKey())
                        .name(dictionary.getValue().getName())
                        .build())
                .collect(Collectors.toList());
    }


    public List<CodeNamePair> getLevel(String project, String... codes) {
        if (project == null) return getLevel();

        FunctionalNumberDictionary current = dictionaries.get(project);

        if (codes.length == 0) {
            return current.getContent().entrySet().stream()
                    .map(entry -> CodeNamePair.builder()
                            .code(entry.getKey())
                            .name(entry.getValue().getName())
                            .build())
                    .collect(Collectors.toList());
        }

        FunctionalNumberDictionary content = current.getContent().get(codes[0]);
        for (int i = 0; i <= codes.length; ) {
            if (content.getContent() == null) {
                return new ArrayList<>();
            }
            if (i + 1 >= codes.length) {
                return getPairs(content.getContent());
            } else {
                content = content.getContent().get(codes[i + 1]);
                i++;
            }
        }

        return Collections.emptyList();
    }

    /**
     * Получает имя объекта словаря по его коду.
     * Один код в аргументах - это АА, два - АА и ВВ, три - АА, ВВ и СС и т.д.
     *
     * @param codes код объекта словаря
     * @return имя объекта словаря
     */
    public List<CodeNamePair> getDictionaryObjectNameByCode(String project, String... codes) {
        List<CodeNamePair> out = new ArrayList<>();

        FunctionalNumberDictionary content = dictionaries.get(project);
        if (codes.length == 0) {
            return content.getContent().entrySet().stream()
                    .map(entry -> CodeNamePair.builder()
                            .code(entry.getKey())
                            .name(entry.getValue().getName())
                            .build())
                    .collect(Collectors.toList());
        }

        content = content.getContent().get(codes[0]);
        if (content != null) {
            for (int i = 0; i <= codes.length; ) {
                out.add(CodeNamePair.builder().code(codes[i]).name(content.getName()).build());
                if (i + 1 >= codes.length) {
                    return out;
                } else {
                    content = content.getContent().get(codes[i + 1]);
                    i++;
                }
            }
        }

        return Collections.emptyList();
    }


}
