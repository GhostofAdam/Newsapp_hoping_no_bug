package com.example.myapplication.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper
{
    /* 单例模式，因sqlite不支持多线程，保证只有一个helper
    *  外部调用getInstance(context)获取helper后，使用
    *  SQLiteDatabase database = helper.getWritebaleDatabase()
    *  获取db，作为参数传入OperateOnSQLite类的函数中*/
    private static SQLiteDbHelper helper;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    public static final String TABLE_COLLECTION = "collection";
    public static final String TABLE_SEEN = "seen";
    static final String TABLE_SEARCH = "search";
    static final String TABLE_STATE = "state";
    public static final String TABLE_SHIELD = "shield";
    private static final String COLLECTION_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COLLECTION + " ("
            + "sole TEXT PRIMARY KEY,"
            + "newsID TEXT,"
            + "title TEXT,"
            + "content TEXT,"
            + "publisher TEXT,"
            + "publishTime TEXT,"
            + "identity TEXT"
            + ");";

    private static final String SEEN_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SEEN + " ("
            + "sole TEXT PRIMARY KEY,"
            + "newsID TEXT,"
            + "title TEXT,"
            + "content TEXT,"
            + "publisher TEXT,"
            + "publishTime TEXT,"
            + "identity TEXT"
            + ");";

    private static final String SEARCH_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SEARCH + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "word TEXT NOT NULL,"
            + "identity TEXT"
            + ");";

    private static final String STATE_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STATE + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "avail INTEGER,"
            + "identity TEXT"
            + ");";

    private static final String SHIELD_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SHIELD + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "sole TEXT,"
            + "word TEXT,"
            + "identity TEXT"
            + ");";

    public SQLiteDbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static SQLiteDbHelper getInstance(Context context)
    {
        if(helper == null)  { helper = new SQLiteDbHelper(context); }
        return helper;
    }

    /* 在Main Activity的OnDestroy()中调用，销毁 */
    @Override
    public synchronized void close()
    {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(COLLECTION_CREATE_TABLE_SQL);
        db.execSQL(SEEN_CREATE_TABLE_SQL);
        db.execSQL(SEARCH_CREATE_TABLE_SQL);
        db.execSQL(STATE_CREATE_TABLE_SQL);
        db.execSQL(SHIELD_CREATE_TABLE_SQL);
    }
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        if(!db.isReadOnly())
        {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}


