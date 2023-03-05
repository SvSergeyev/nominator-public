package com.cml.defaultnominator.config.database.common;

public class DatabaseConfigurationPropertiesNames {
    public static final String CORE_PERSISTENCE_UNIT_NAME = "Core";
    public static final String CORE_REPOSITORY_PACKAGE = "com.cml.defaultnominator.dao.remote.core";
    public static final String CORE_PROPERTY_PREFIX = "app.database.users";
    public static final String CORE_DATABASE_PROPERTY = "CoreProperty";
    public static final String CORE_DATABASE_DATA_SOURCE = "CoreDataSource";
    public static final String CORE_DATABASE_ENTITY_MANAGER_FACTORY = "CoreEntityManagerFactory";
    public static final String CORE_DATABASE_TRANSACTION_MANAGER = "CoreTransactionManager";
    public static final String CORE_PACKAGES_TO_SCAN = "com.cml.defaultnominator.entity.remote.core.user," +
            "com.cml.defaultnominator.entity.remote.core.approval," +
            "com.cml.defaultnominator.entity.remote.core.tree";

    public static final String NAMED_OBJECTS_PERSISTENCE_UNIT_NAME = "NamedObjects";
    public static final String NAMED_OBJECTS_REPOSITORY_PACKAGE = "com.cml.defaultnominator.dao.internal";
    public static final String NAMED_OBJECTS_PROPERTY_PREFIX = "app.database.elements";
    public static final String NAMED_OBJECTS_DATABASE_PROPERTY = "NamedObjectsProperty";
    public static final String NAMED_OBJECTS_DATABASE_DATA_SOURCE = "NamedObjectsDataSource";
    public static final String NAMED_OBJECTS_DATABASE_ENTITY_MANAGER_FACTORY = "NamedObjectsEntityManagerFactory";
    public static final String NAMED_OBJECTS_DATABASE_TRANSACTION_MANAGER = "NamedObjectsTransactionManager";
    public static final String NAMED_OBJECTS_PACKAGES_TO_SCAN = "com.cml.defaultnominator.entity.internal.namedobject," +
            "com.cml.defaultnominator.entity.internal.container," +
            "com.cml.defaultnominator.entity.internal.target";

    public static final String TARGET_LIBRARY_PERSISTENCE_UNIT_NAME = "TargetLibrary";
    public static final String TARGET_LIBRARY_REPOSITORY_PACKAGE = "com.cml.defaultnominator.dao.remote.targetlibrary";
    public static final String TARGET_LIBRARY_PROPERTY_PREFIX = "app.database.targetlibrary";
    public static final String TARGET_LIBRARY_DATABASE_PROPERTY = "TargetLibraryProperty";
    public static final String TARGET_LIBRARY_DATABASE_DATA_SOURCE = "TargetLibraryDataSource";
    public static final String TARGET_LIBRARY_DATABASE_ENTITY_MANAGER_FACTORY = "TargetLibraryEntityManagerFactory";
    public static final String TARGET_LIBRARY_DATABASE_TRANSACTION_MANAGER = "TargetLibraryTransactionManager";
    public static final String TARGET_LIBRARY_PACKAGES_TO_SCAN = "com.cml.defaultnominator.entity.remote.targetlibrary";
}
