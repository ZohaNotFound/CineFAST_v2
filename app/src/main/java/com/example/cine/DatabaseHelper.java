package com.example.cine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cine.db";
    private static final int DATABASE_VERSION = 1;

    // Table Snacks
    public static final String TABLE_SNACKS = "snacks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SNACKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_IMAGE + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        addSnack(db, "Popcorn", 9.0f, R.drawable.popcorn);
        addSnack(db, "Nachos", 8.0f, R.drawable.nachos);
        addSnack(db, "Drink", 6.0f, R.drawable.drink);
        addSnack(db, "Candy", 7.0f, R.drawable.candy);
    }

    private void addSnack(SQLiteDatabase db, String name, float price, int imageResId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, imageResId);
        db.insert(TABLE_SNACKS, null, values);
    }

    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SNACKS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                snacks.add(new Snack(name, price, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snacks;
    }
}