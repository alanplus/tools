package com.android.tools.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * sqlite数据库管理类，负责表的初始化和表的更
 *
 * @author Administrator
 */
public class SQLiteManager extends SQLiteOpenHelper {
    private static volatile SQLiteManager instance;
    private Context mContext;

    private static DatabaseConfig mConfig;
    private static volatile SQLiteDatabase db;

    private SQLiteManager(Context context, String name, int version) {
        super(context, name, null, version);
        mContext = context;
    }

    static synchronized SQLiteDatabase getDB(Context context,
                                             DatabaseConfig config) {
        if (db == null) {
            mConfig = config;
            if (instance == null) {
                instance = new SQLiteManager(context, config.getDatabaseName(),
                        config.getDatabaseVersion());
            }
            db = instance.getWritableDatabase();
//            db.execSQL("attach DATABASE " + "'/data/data/" + context.getPackageName() + "/databases/weici_ext.db' AS 'c'");
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<Class<? extends SQLiteTable>> classes = mConfig.getTables(mContext);
        for (Class<? extends SQLiteTable> clazz : classes) {
            try {
                Constructor<? extends SQLiteTable> con = clazz
                        .getConstructor(Context.class);
                SQLiteTable table = con.newInstance(mContext);
                table.onCreate(db);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<Class<? extends SQLiteTable>> classes = mConfig.getTables(mContext);
        for (Class<? extends SQLiteTable> clazz : classes) {
            try {
                Constructor<? extends SQLiteTable> con = clazz
                        .getConstructor(Context.class);
                SQLiteTable table = con.newInstance(mContext);
                table.onUpdate(db, oldVersion, newVersion);
            } catch (Exception e) {

            }
        }
    }

    /**
     * DAO表接口
     *
     * @author DuShengjun<dushengjun   @   gmail.com>
     */
    public interface SQLiteTable {
        String COL_TYPE_AUTO_ID = "INTEGER PRIMARY KEY";
        String COL_TYPE_FLOAT = "FLOAT";
        String COL_TYPE_TEXT = "TEXT";
        String COL_TYPE_INT = "INT";
        String COL_TYPE_LONG = "LONG";

        String COL_TYPE_PRIMARY_KEY = "PRIMARY KEY";
        String COL_TYPE_NOT_NULL = " NOT NULL ";
        String COL_TYPE_DEFAULT = " DEFAULT(%s) ";

        void onCreate(SQLiteDatabase database);

        void onUpdate(SQLiteDatabase database, int oldVersion,
                      int newVersion);
    }

    public static void destroy() {
        if (null != db) {
            db.close();
            db = null;
            instance = null;
        }
    }

}
