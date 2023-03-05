package com.cml.defaultnominator.config.database.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSourceProperties {

    String prefix;
    String url;
    String username;
    String password;
    String driver;
    String dialect;

}
