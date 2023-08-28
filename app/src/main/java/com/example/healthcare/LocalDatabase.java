package com.example.healthcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalDatabase extends SQLiteOpenHelper {

    public LocalDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE users(firstname text, lastname text, username text, password text)";
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void register(String firstname, String lastname, String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("username", username);
        cv.put("password", encrypt(password));

        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }

    public boolean userIsNew(String username) {
        String args[] = new String[1];
        args[0] = username;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username = ?", args);

        return !c.moveToFirst();
    }

    public boolean areValidCredentials(String username, String password) {
        String args[] = new String[2];
        args[0] = username;
        args[1] = encrypt(password);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", args);

        return c.moveToFirst();
    }

    private String encrypt(String input) {
        int hash = 0;

        for (int i = 0; i < input.length(); i++) {
            hash = (hash << 5) - hash + input.charAt(i);
        }

        return Integer.toHexString(hash);
    }
}
