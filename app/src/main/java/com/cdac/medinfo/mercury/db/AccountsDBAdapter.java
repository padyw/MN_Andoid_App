package com.cdac.medinfo.mercury.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccountsDBAdapter 
{
    public static final String _ID = "_id";
	public static final String SERVER_URL = "server_url";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CREATED_AT = "created_at";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mContext;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public AccountsDBAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Open the accounts database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public AccountsDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mContext);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
    }

    /**
     * Create a new account. If the account is successfully created return the new
     * rowId for that account, otherwise return a -1 to indicate failure.
     * 
     * @param name
     * @param model
     * @param year
     * @return rowId or -1 if failed
     */
    public long createAccount(String strAccountID, String strServerURL, String strLoginID, String strPassword, long lCreatedAt){
        ContentValues initialValues = new ContentValues();
        initialValues.put(_ID, strAccountID);
        initialValues.put(SERVER_URL, strServerURL);
        initialValues.put(USERNAME, strLoginID);
        initialValues.put(PASSWORD, strPassword);
        initialValues.put(CREATED_AT, lCreatedAt);
        return this.mDb.insert(DBAdapter.DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the Account with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteAccount(String strAccountID) {

        return this.mDb.delete(DBAdapter.DATABASE_TABLE, _ID + "='" + strAccountID + "'", null) > 0;
    }

    /**
     * Return a Cursor over the list of all Accounts in the database
     * 
     * @return Cursor over all Accounts
     */
    public Cursor getAllAccounts() {

        return this.mDb.query(DBAdapter.DATABASE_TABLE, new String[] { _ID, SERVER_URL, USERNAME, PASSWORD, CREATED_AT}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the Account that matches the given AccountID
     * @param strAccountID
     * @return Cursor positioned to matching Account, if found
     * @throws SQLException if Account could not be found/retrieved
     */
    public Cursor getAccount(String strAccountID) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DBAdapter.DATABASE_TABLE, new String[] { _ID, SERVER_URL, USERNAME, PASSWORD, CREATED_AT}, _ID + "='" + strAccountID + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the Account.
     * 
     * @param rowId
     * @param name
     * @param model
     * @param year
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateAccount(String strAccountID, String strServerURL, String strLoginID, String strPassword, long lCreatedAt)
    {
        ContentValues args = new ContentValues();
        args.put(SERVER_URL, strServerURL);
        args.put(USERNAME, strLoginID);
        args.put(PASSWORD, strPassword);
        args.put(CREATED_AT, lCreatedAt);

        return this.mDb.update(DBAdapter.DATABASE_TABLE, args, _ID + "='" + strAccountID + "'", null) >0; 
    }
}
