package com.android.tools.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mouse on 2018/12/7.
 */
public class ExternalSqliteManager {

    private static volatile ExternalSqliteManager instance;
    private Context mContext;
    private static volatile SQLiteDatabase sqLiteDatabase;
    private String databaseName;

    private ExternalSqliteManager(Context context, String databaseName) {
        mContext = context.getApplicationContext();
        this.databaseName = databaseName;
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/" + context.getPackageName() + "/databases/" + databaseName, null);
    }

    public static synchronized SQLiteDatabase getDB(Context context, String databaseName) {
        if (sqLiteDatabase == null) {
            if (instance == null) {
                instance = new ExternalSqliteManager(context, databaseName);
            }
        }
        return instance.getSqLiteDatabase();
    }


    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
