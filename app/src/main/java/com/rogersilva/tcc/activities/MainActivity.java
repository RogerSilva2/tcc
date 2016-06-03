package com.rogersilva.tcc.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rogersilva.tcc.R;
import com.rogersilva.tcc.helpers.SqlOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;


public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("sqlndk");
    }

    private native String invokeNativeFunction();

    static Context context;
    static Object system;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        system = getSystemService(Context.TELEPHONY_SERVICE);

        String key = invokeNativeFunction();
        initializeSQLCipher(key);
    }

    private void initializeSQLCipher(String password) {
        // Carregando as libs da biblioteca de encriptação
        SQLiteDatabase.loadLibs(this);
        SqlOpenHelper helper = new SqlOpenHelper(this);
        // Trazendo o banco de dados da nossa aplicação, e adicionando senha de encriptação.
        SQLiteDatabase db = helper.getWritableDatabase(password);

        selectExampleValue(db);
    }

    private void clickButton() {
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText passwordEdit = (EditText) findViewById(R.id.password_edit);
                initializeSQLCipher(passwordEdit.getText().toString());
            }
        });
    }

    private void savedLastCredentials() {
        SharedPreferences sharedPrefs = getSharedPreferences("PrefsTCC", MODE_PRIVATE);

        String username = ((EditText) findViewById(R.id.username_edit)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_edit)).getText().toString();

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(SettingsActivity.LAST_USERNAME_KEY, username);
        editor.putString(SettingsActivity.LAST_PASSWORD_KEY, password);
        editor.apply();
    }

    private void loadLastCredentials() {
        SharedPreferences sharedPrefs = getSharedPreferences("PrefsTCC", MODE_PRIVATE);

        String username = sharedPrefs.getString(SettingsActivity.LAST_USERNAME_KEY, "");
        String password = sharedPrefs.getString(SettingsActivity.LAST_PASSWORD_KEY, "");

        ((EditText) findViewById(R.id.username_edit)).setText(username);
        ((EditText) findViewById(R.id.password_edit)).setText(password);
    }

    private static String generateKey() {
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        TelephonyManager manager = (TelephonyManager) system;
        String deviceId = manager.getDeviceId();

        String str = Build.BOARD + Build.BRAND + Build.CPU_ABI + Build.DEVICE + Build.DISPLAY +
                Build.FINGERPRINT + Build.HOST + Build.ID + Build.MANUFACTURER + Build.MODEL +
                Build.PRODUCT + Build.TAGS + Build.TYPE + Build.USER;

        return md5(str + deviceId + androidId);
    }

    private static String md5(String str) {
        return str;
    }

    /**
     * Chamado quando se quer inserir um usuário no banco de dados.
     *
     * @param db O banco de dados
     */
    private void insertExampleValue(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SqlOpenHelper.FIRST, "Roger");
        values.put(SqlOpenHelper.LAST, "Silva");
        values.put(SqlOpenHelper.USERNAME, "rogersilva");
        values.put(SqlOpenHelper.PASSWORD, "qwe123");

        Log.d("User insert", String.valueOf(db.insert(SqlOpenHelper.TABLE_NAME, null, values)));
    }

    /**
     * Chamado quando se quer mostrar os dados de um usuário que estam salvos no banco de dados.
     *
     * @param db O banco de dados
     */
    private void selectExampleValue(SQLiteDatabase db) {
        Cursor cursor = db.query(SqlOpenHelper.TABLE_NAME,
                new String[] { SqlOpenHelper.ID, SqlOpenHelper.FIRST, SqlOpenHelper.LAST,
                        SqlOpenHelper.USERNAME, SqlOpenHelper.PASSWORD },
                null, null, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                Log.d("id", String.valueOf(cursor.getLong(0)));
                Log.d("first name", cursor.getString(1));
                Log.d("last name", cursor.getString(2));
                Log.d("username", cursor.getString(3));
                Log.d("password", cursor.getString(4));
            } while (cursor.moveToNext());
        } else {
            insertExampleValue(db);
        }
        cursor.close();
    }

    private boolean checkLogin(SQLiteDatabase db, String username, String password) {
        boolean checked = false;

        Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = '" + username +
                "' AND password = '" + password + "';", null);

        if (cursor != null) {
            if (cursor.moveToFirst())
                checked = true;
            cursor.close();
        }

        return checked;
    }

    private boolean checkSecureLogin(SQLiteDatabase db, String username, String password) {
        boolean checked = false;

        Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = ?" +
                " AND password = ?", new String[]{ username, password });

        if (cursor != null) {
            if (cursor.moveToFirst())
                checked = true;
            cursor.close();
        }

        return checked;
    }
}
