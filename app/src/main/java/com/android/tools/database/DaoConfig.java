package com.android.tools.database;

/**
 * Created by Mouse on 2018/4/5.
 */

public class DaoConfig {

    private static DaoConfig config;
    private DatabaseConfig databaseConfig;

    private DaoConfig() {

    }

    public static DaoConfig getConfig() {

        if (null == config) {
            config = new DaoConfig();
        }
        return config;
    }

    public void init(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

}
