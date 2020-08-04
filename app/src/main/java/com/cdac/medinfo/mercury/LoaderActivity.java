package com.cdac.medinfo.mercury;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoaderActivity.this);

                    boolean isLoggedIn  = sharedPreferences.getBoolean(getString(R.string.is_user_logged_in), false);

                    if ( isLoggedIn ){
                        Intent intent = new Intent(LoaderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(LoaderActivity.this, AccountsViewActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}