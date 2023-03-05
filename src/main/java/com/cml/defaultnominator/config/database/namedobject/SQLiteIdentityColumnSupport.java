package com.cml.defaultnominator.config.database.namedobject;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

/**
 * Реализация поддержки identity column для SQlite3. Подробнее см. {@linkplain org.hibernate.dialect.identity.IdentityColumnSupport}
 */
public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
    @Override
    public boolean supportsIdentityColumns() {
        return super.supportsIdentityColumns();
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        return super.getIdentitySelectString(table, column, type);
    }

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return super.getIdentityColumnString(type);
    }
}
