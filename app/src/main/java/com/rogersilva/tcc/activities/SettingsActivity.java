package com.rogersilva.tcc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rogersilva.tcc.R;


public class SettingsActivity extends AppCompatActivity {

    public static final String LAST_USERNAME_KEY = "lastUsernameKey";
    public static final String LAST_PASSWORD_KEY = "lastPasswordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
