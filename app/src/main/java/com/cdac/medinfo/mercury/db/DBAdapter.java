package com.cdac.medinfo.mercury.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    public static final String DATABASE_NAME = "mercury";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "account";

    private static final String CREATE_TABLE_ACCOUNTS = "create table " + DATABASE_TABLE + " (" + AccountsDBAdapter._ID + " VARCHAR(36) primary key , "
    	+ AccountsDBAdapter.SERVER_URL+ " TEXT,"
    	+ AccountsDBAdapter.USERNAME + " TEXT,"
    	+ AccountsDBAdapter.PASSWORD + " TEXT,"
    	+ AccountsDBAdapter.CREATED_AT+ " INT" + ");";
    
    private final Context context;
    private DatabaseHelper mDbHelper;
    /**
     * Constructor
     * @param ctx
     */
    public DBAdapter(Context context)
    {
    	 this.context = context;
    	 mDbHelper = new DatabaseHelper(this.context);
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            
            //open database 
            getReadableDatabase();
           // getWritableDatabase();
        }

        // Called only once, first time the DB is created
        @Override
        public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE_ACCOUNTS); }

        // Called whenever newVersion != oldVersion
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {               
            // Adding any table modification. // Typically do ALTER TABLE statements
        }
    }
}
