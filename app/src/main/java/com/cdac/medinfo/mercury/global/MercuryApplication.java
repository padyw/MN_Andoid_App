package com.cdac.medinfo.mercury.global;

import android.app.Application;

import com.cdac.medinfo.mercury.db.DBAdapter;

public class MercuryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DBAdapter objDbAdapter = new DBAdapter(this);
        objDbAdapter.close();
    }
}
