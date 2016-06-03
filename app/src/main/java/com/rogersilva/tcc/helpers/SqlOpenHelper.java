package com.rogersilva.tcc.helpers;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class SqlOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "usersdb.sqlite";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "Users";
    public static final String ID = "id";
    public static final String FIRST = "first_name";
    public static final String LAST = "last_name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public SqlOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     * Chamado quando se quer criar a tabela usuário no banco de dados.
     *
     * @param db O banco de dados
     */
    private void createDatabase(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                        ID + " integer primary key autoincrement not null, " +
                        FIRST + " text not null, " +
                        LAST + " text not null, " +
                        USERNAME + " text not null, " +
                        PASSWORD + " text not null "
                        + ");"
        );
    }
}
