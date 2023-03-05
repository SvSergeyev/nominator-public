package com.cml.defaultnominator.dto.dictionary.fn;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@Builder
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
public class CodeNamePair {
    String code;
    String name;
}
