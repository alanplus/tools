package com.android.tools.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IBaseDAO<T> {
    SQLiteDatabase getDatabase();

    String getTableName();

    void deleteAll();

    int countAll();

    List<T> findAll();

}
