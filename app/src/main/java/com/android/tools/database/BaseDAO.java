package com.android.tools.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author Administrator
 */
public abstract class BaseDAO<T> implements SQLiteManager.SQLiteTable {
    public static final String[] ALL_COLS = new String[]{"*"};
    protected Context mContext;
    protected String mTableName;
    private DatabaseConfig mConfig;
    private List<Class<? extends IPatcher<T>>> mPatcherList;

    protected OnGetData<T> onGetData;


    /**
     * @param context
     */
    public BaseDAO(String tableName, Context context, DatabaseConfig config) {
        mContext = context;
        mTableName = tableName;
        mConfig = config;
    }

    public BaseDAO(String tableName, Context context) {
        mContext = context;
        mTableName = tableName;
    }
    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    /**
     * 获取sqlite数据
     *
     * @return
     * @author dushengjun
     */
    public SQLiteDatabase getDatabase() {
        return SQLiteManager.getDB(mContext, mConfig);
    }

    /**
     * 从游标中取一个对
     *
     * @param cursor
     * @param i
     * @return
     * @author dushengjun
     */
    protected abstract T findByCursor(Cursor cursor, int i);

    private T findByCursorWithCallback(Cursor cursor, int i){
        T t = findByCursor(cursor, 0);
        if(null!=onGetData){
            onGetData.onGetData(t,cursor);
        }
        return t;
    }

    @Override
    public final void onUpdate(SQLiteDatabase database, int oldVersion,
                               int newVersion) {
        if (this.mPatcherList != null) {
            for (Class<? extends IPatcher<T>> patcherClazz : mPatcherList) {
                IPatcher<T> inst;
                try {
                    inst = patcherClazz.newInstance();
                    int max = inst.getSupportMaxVersion();
                    if (oldVersion <= max) {
                        inst.execute(this, database, getContext());
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 根据游标获取列表
     *
     * @param cursor
     * @return
     * @author dushengjun
     */
    public List<T> findListByCursor(Cursor cursor) {
        return findListByCursor(cursor, cursor.getCount());
    }

    public List<T> findListBySql(String sql) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        return findListByCursor(cursor, cursor.getCount());
    }

    public void findListBySql(String sql, OnGetDataObject<T> onGetDataObject, List<T> ret) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        int n = 0;
        try {
            while (!cursor.isAfterLast() && (n < size)) {
                T t = onGetDataObject.onGetDataObject(cursor);
                if(checkItem(t)){
                    ret.add(t);
                }
                cursor.moveToNext();
                n++;
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
    }

    public void findListBySql(String sql, OnGetDataObject<T> onGetDataObject) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        int n = 0;
        try {
            while (!cursor.isAfterLast() && (n < size)) {
                onGetDataObject.onGetDataObject(cursor);
                cursor.moveToNext();
                n++;
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
    }

    public T findBySql(String sql, OnGetDataObject<T> onGetDataObject) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        T t = null;
        try {
            if(cursor != null && cursor.moveToFirst()) {
                t = onGetDataObject.onGetDataObject(cursor);
            }
        }finally {
            cursor.close();
        }

        return t;
    }

    public T findBySql(String sql) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        T t = null;
        try {
            if(cursor != null && cursor.moveToFirst()) {
                t = findByCursorWithCallback(cursor,cursor.getCount());
            }
        }finally {
            cursor.close();
        }
        return t;
    }


    public int countListBySql(String sql) {
        Cursor cursor = getDatabase().rawQuery(sql, null);
        return countByCursor(cursor);
    }

    public int countByCursor(Cursor cursor){
        int count =0;
        try {
            if(cursor.moveToFirst()){
                count = getIntFromCusor(cursor, "cc");
            }
        }finally {
            cursor.close();
            onGetData = null;
        }
        return count;
    }

    /**
     * 根据游标获取指定个数的列
     *
     * @param cursor
     * @param size
     * @return
     * @author dushengjun
     */
    public List<T> findListByCursor(Cursor cursor, int size) {
        List<T> ret = new ArrayList<T>();
        cursor.moveToFirst();
        int n = 0;
        try {
            while (!cursor.isAfterLast() && (n < size)) {
                T t = findByCursorWithCallback(cursor,n);
                if(checkItem(t)){
                    ret.add(t);
                }
                cursor.moveToNext();
                n++;
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
        return ret;
    }

    public List<T> findListByCursor(Cursor cursor, RowMapper<T> mapper) {
        List<T> ret = new ArrayList<T>();
        cursor.moveToFirst();
        int n = 0;
        while (!cursor.isAfterLast()) {
            T t = findByCursorWithCallback(cursor,n);
            if (mapper != null) {
                mapper.map(t, n);
            }
            ret.add(t);
            cursor.moveToNext();
            n++;
        }
        cursor.close();
        return ret;
    }

    public void findByCursor(Cursor cursor, OnEachListener<T> listener) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T t = findByCursorWithCallback(cursor, 0);
            if (listener != null) {
                listener.onEach(t);
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     * 根据表明和排序获取指定列的列
     *
     * @param orderBy   排序id desc createAt asc 无需增加order by
     * @param rows      查询
     * @return 对象列表
     * @author dushengjun
     */
    public List<T> findAll(String orderBy, String... rows) {
        Cursor cursor = getDatabase().query(mTableName, rows, null, null, null,
                null, orderBy);
        return findListByCursor(cursor);
    }

    public List<T> findAll(SQLiteDatabase database, String orderBy) {
        Cursor cursor = database.query(getTableName(), ALL_COLS, null, null,
                null, null, orderBy);
        return findListByCursor(cursor);
    }

    public List<T> findAll(String orderBy, int start, int count, String... rows) {
        Cursor cursor = getDatabase().query(mTableName, rows, null, null, null,
                null, orderBy, start + "," + count);
        return findListByCursor(cursor);
    }

    /**
     * 删除全部
     *
     * @author dushengjun
     */
    public void deleteAll() {
        getDatabase().delete(mTableName, null, null);
    }

    protected void deleteAll(SQLiteDatabase database) {
        database.delete(mTableName, null, null);
    }

    public String asString(Object object) {
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    protected void dropTable(SQLiteDatabase database) {
        String sql = "DROP TABLE IF EXISTS " + mTableName;
        database.execSQL(sql);
    }

    /**
     * 执行sql语句
     *
     * @param database
     * @param sqls
     */
    protected void execSQLs(SQLiteDatabase database, List<String> sqls) {
        if (sqls == null) {
            return;
        }

        for (String sql : sqls) {
            database.execSQL(sql);
        }
    }

    public List<T> findAll() {
        Cursor cursor = getDatabase().query(mTableName, ALL_COLS, null, null,
                null, null, null);
        return findListByCursor(cursor);
    }

    public int countAll() {
        Cursor cursor = getDatabase().query(mTableName,
                new String[]{"COUNT(*)"}, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
        return 0;
    }

    /**
     * 创建表
     *
     * @param database
     * @param columns
     */
    protected void createTable(SQLiteDatabase database,
                               Map<String, String> columns) {

        String primaryKey = null;
        String value = null;

        if(columns.containsKey(COL_TYPE_PRIMARY_KEY)){
            primaryKey = COL_TYPE_PRIMARY_KEY;
            value = columns.remove(COL_TYPE_PRIMARY_KEY);
        }

        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE " + mTableName + "(");
        Object[] keys = columns.keySet().toArray();
        for (Object key : keys) {
            sql.append(key + " " + columns.get(key));
            if (key != keys[keys.length - 1]) {
                sql.append(",");
            }
        }
        if(!TextUtils.isEmpty(primaryKey)){
            sql.append(",");
            sql.append(primaryKey+" "+value);
        }
        sql.append(")");
        database.execSQL(sql.toString());
    }

    protected long save(ContentValues values) {
        return getDatabase().insert(mTableName, null, values);
    }

    protected long save(SQLiteDatabase database, ContentValues values) {
        return database.insert(getTableName(), null, values);
    }

    public void addColumns(SQLiteDatabase database, Map<String, String> columns) {
        if (columns == null) {
            return;
        }
        Object[] keys = columns.keySet().toArray();
        String sql = null;
        for (Object key : keys) {
            sql = "ALTER TABLE " + mTableName + " ADD `" + key.toString()
                    + "` " + columns.get(key);
            database.execSQL(sql);
        }
    }

    public String getTableName() {
        return mTableName;
    }

    @SuppressWarnings("deprecation")
    protected Cursor query(String[] columns, String selection,
                           String[] selectionArgs, String orderBy, String limit) {
        if (Integer.valueOf(android.os.Build.VERSION.SDK) <= 3) {
            // 1.5貌似就没办法支持limit
            limit = null;
        }
        return getDatabase().query(getTableName(), columns, selection,
                selectionArgs, null, null, orderBy, limit);
    }

    @SuppressWarnings("deprecation")
    protected Cursor query(String[] columns, String selection,
                           String[] selectionArgs, String limit) {
        if (Integer.valueOf(android.os.Build.VERSION.SDK) <= 3) {
            // 1.5貌似就没办法支持limit
            limit = null;
        }
        return getDatabase().query(getTableName(), columns, selection,
                selectionArgs, null, null, null, limit);
    }

    /**
     * 查找对象
     *
     * @param columns
     * @param selection
     * @param selectionArgs
     * @return
     */
    protected T find(SQLiteDatabase database, String[] columns,
                     String selection, String[] selectionArgs) {
        T ret = null;
        Cursor cursor = database.query(getTableName(), columns, selection,
                selectionArgs, null, null, null);
        if(cursor == null) return null;
        try {
            if (cursor.moveToFirst()) {
                ret = findByCursorWithCallback(cursor,cursor.getCount());
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
        return ret;
    }

    protected T find(String[] columns, String selection, String[] selectionArgs) {
        return find(getDatabase(), columns, selection, selectionArgs);
    }

    protected List<T> findList(String[] columns, String selection,
                               String[] selectionArgs, String orderBy) {
        Cursor cursor = getDatabase().query(getTableName(), columns, selection,
                selectionArgs, null, null, orderBy);
        return findListByCursor(cursor);
    }

    protected List<T> findList(String[] columns, String selection,
                               String[] selectionArgs, String orderBy, String limit) {
        Cursor cursor = getDatabase().query(getTableName(), columns, selection,
                selectionArgs, null, null, orderBy, limit);
        return findListByCursor(cursor);
    }

    protected int count(String selection, String[] selectionArgs) {
        Cursor cursor = getDatabase().query(getTableName(),
                new String[]{"count(*)"}, selection, selectionArgs, null,
                null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
            onGetData = null;
        }
        return 0;
    }

    protected Context getContext() {
        return mContext;
    }

    public interface RowMapper<T> {
        public void map(T t, int index);
    }

    public interface OnEachListener<T> {
        public void onEach(T t);
    }

    /**
     * 注册补丁类,此类必须拥有用无参数的构造函数
     *
     * @param patcherClazz
     */
    public BaseDAO<T> registerPatcher(Class<? extends IPatcher<T>> patcherClazz) {
        if (mPatcherList == null) {
            mPatcherList = new ArrayList<Class<? extends IPatcher<T>>>();
        }
        mPatcherList.add(patcherClazz);
        return this;
    }

    protected final boolean exist(SQLiteDatabase database, String selection,
                                  String[] args) {
        Cursor cursor = database.query(getTableName(), ALL_COLS, selection,
                args, null, null, null);
        try {
            return cursor != null && cursor.getCount() > 0;
        } finally {
            onGetData = null;
            if (cursor != null)
                cursor.close();
        }
    }

    protected final boolean exist(String selection, String[] args) {
        return exist(getDatabase(), selection, args);
    }

    protected int getIntFromCusor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) return 0;
        return cursor.getInt(columnIndex);
    }

    protected String getStringFromCusor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) return "";
        return cursor.getString(columnIndex);
    }

    protected Long getLongFromCusor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) return 0L;
        return cursor.getLong(columnIndex);
    }

    protected Float getFloatFromCusor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) return 0F;
        return cursor.getFloat(columnIndex);
    }

    protected final String[] args(Object... args) {
        int length = args.length;
        String[] arg = new String[args.length];
        for (int i = 0; i < length; i++) {
            arg[i] = String.valueOf(args[i]);
        }
        return arg;
    }

    protected final boolean update(ContentValues values, String selection, String[] args){
        int res = getDatabase().update(getTableName(), values, selection, args);
        return res == 1;
    }

    public interface OnGetData<T>{
        void onGetData(T t, Cursor cursor);
    }

    public boolean checkItem(T t){
        return t!=null;
    }

    public interface OnGetDataObject<T>{
        T onGetDataObject(Cursor cursor);
    }

    protected static String replaceSpecialChar(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        return string.replace("'", "''");
    }
}
