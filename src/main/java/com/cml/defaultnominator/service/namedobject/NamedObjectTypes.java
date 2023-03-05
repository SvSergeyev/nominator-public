package com.cml.defaultnominator.service.namedobject;

import lombok.Getter;

public enum NamedObjectTypes {
    SIM("simulation"),
    STP("submodeltype"),
    DMU("dmu"),
    WIKI("wikipage"),
    FLD("folder"),
    LCS("loadcase"),
    CRT("chart"),
    PRT("part"),
    ISS("issue"),
    CSUB("complexsubmodel"),

    PRJ("project"),

    STB("storyboard"),
    DOC("document"),

    TAR("target"),
    TGR("targetgroup");

    @Getter
    private final String fullName;

    NamedObjectTypes(String fullName) {
        this.fullName = fullName;
    }

    public static NamedObjectTypes formatToNamedObjectType(String text) {
        String formattedName = text.replaceAll("[^A-Za-z]", "");
        for (NamedObjectTypes type : NamedObjectTypes.values()) {
            if (type.getFullName().equalsIgnoreCase(formattedName) || type.name().equalsIgnoreCase(formattedName)) {
                return type;
            }
        }
        return null;
    }

    public static String formatFullTypeNameToShort(String fullName) {
        String formattedName = fullName.replaceAll("[^A-Za-z]", "");
        for (NamedObjectTypes type : NamedObjectTypes.values()) {
            if (type.getFullName().equalsIgnoreCase(formattedName)) {
                return type.name();
            }
            else if (type.name().equalsIgnoreCase(formattedName)) {
                return fullName;
            }
        }
        return null;
    }
}
