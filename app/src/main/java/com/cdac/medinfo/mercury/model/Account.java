package com.cdac.medinfo.mercury.model;

import android.content.Context;
import android.database.Cursor;

import com.cdac.medinfo.mercury.db.AccountsDBAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable {

    String id;
    String serverURL;
    String username;
    String password;
    long createdAt;

    public Account() {
        id = UUID.randomUUID().toString();
        serverURL = new String();
        username = new String();
        password =  new String();
        createdAt = System.currentTimeMillis();
    }

    public Account(String serverURL, String username, String password) {
        id = UUID.randomUUID().toString();
        this.serverURL = serverURL;
        this.username = username;
        this.password = password;
        createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public void createAccount(Context context) {
        // Insert the data
        AccountsDBAdapter dbAdapter = new AccountsDBAdapter(context);
        dbAdapter.open();
        dbAdapter.createAccount(id, serverURL, username, password, createdAt);
        dbAdapter.close();
    }

    public void updateAccount(Context context) {
        // update the data
        AccountsDBAdapter dbAdapter = new AccountsDBAdapter(context);
        dbAdapter.open();
        dbAdapter.updateAccount(id, serverURL, username, password, createdAt);
        dbAdapter.close();
    }

    public static Account getAccountDetails(Context context, String strAccountID) {

        // get the data
        Account objAccount = null;
        AccountsDBAdapter dbAdapter = new AccountsDBAdapter(context);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAccount(strAccountID);
        if (cursor.moveToFirst())
        {
            int iAccountIDColumn = cursor.getColumnIndex(AccountsDBAdapter._ID);
            int iServerURLColumn = cursor.getColumnIndex(AccountsDBAdapter.SERVER_URL);
            int iLoginIDColumn = cursor.getColumnIndex(AccountsDBAdapter.USERNAME);
            int iPasswordColumn = cursor.getColumnIndex(AccountsDBAdapter.PASSWORD);
            int iCreatedAtColumn = cursor.getColumnIndex(AccountsDBAdapter.CREATED_AT);
            do
            {
                objAccount = new Account();
                // Get the field values
                objAccount.setId(cursor.getString(iAccountIDColumn));
                objAccount.setServerURL(cursor.getString(iServerURLColumn));
                objAccount.setUsername(cursor.getString(iLoginIDColumn));
                objAccount.setPassword(cursor.getString(iPasswordColumn));
                objAccount.setCreatedAt(Long.parseLong(cursor.getString(iCreatedAtColumn)));

            } while (cursor.moveToNext());
        }
        dbAdapter.close();
        return objAccount;
    }
    public static List<Account> getAllAccounts(Context context) {
        // get the data
        AccountsDBAdapter dbAdapter = new AccountsDBAdapter(context);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAllAccounts();
        List<Account> listAccounts = new ArrayList<Account>();
        if (cursor.moveToFirst())
        {
            int iAccountIDColumn = cursor.getColumnIndex(AccountsDBAdapter._ID);
            int iServerURLColumn = cursor.getColumnIndex(AccountsDBAdapter.SERVER_URL);
            int iLoginIDColumn = cursor.getColumnIndex(AccountsDBAdapter.USERNAME);
            int iPasswordColumn = cursor.getColumnIndex(AccountsDBAdapter.PASSWORD);
            int iCreatedAtColumn = cursor.getColumnIndex(AccountsDBAdapter.CREATED_AT);
            do
            {
                Account objAccount = new Account();
                // Get the field values
                objAccount.setId(cursor.getString(iAccountIDColumn));
                objAccount.setServerURL(cursor.getString(iServerURLColumn));
                objAccount.setUsername(cursor.getString(iLoginIDColumn));
                objAccount.setPassword(cursor.getString(iPasswordColumn));
                objAccount.setCreatedAt(Long.parseLong(cursor.getString(iCreatedAtColumn)));

                // Fill accounts list
                listAccounts.add(objAccount);

            } while (cursor.moveToNext());
        }
        dbAdapter.close();
        return listAccounts;
    }

    public static void deleteAccount(Context context, String strAccountID) {

        // Delete the data
        AccountsDBAdapter dbAdapter = new AccountsDBAdapter(context);
        dbAdapter.open();
        dbAdapter.deleteAccount(strAccountID);
        dbAdapter.close();
    }

}
