package com.example.myapplication.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper
{
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "database.db";
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_COLLECTION = "collection";
    private static final String ACCOUNT_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACCOUNT + " ("
            + "identity vachar(20) PRIMARY KEY NOT NULL,"
            + "password vachar(20) NOT NULL"
            + ");";
    private static final String COLLECTION_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COLLECTION + " ("
            + "newsID varchar(100) PRIMARY KEY NOT NULL,"
            + "title varchar,"
            + "content varchar,"
            + "account varchar,"
            + "FOREIGN KEY(account) REFERENCES account(identity)"
            + ");";

    public SQLiteDbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ACCOUNT_CREATE_TABLE_SQL);
        db.execSQL(COLLECTION_CREATE_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}


