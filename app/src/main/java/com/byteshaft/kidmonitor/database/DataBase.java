package com.byteshaft.kidmonitor.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private ArrayList<OnDatabaseChangedListener> mListeners = new ArrayList<>();

    public DataBase(Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseConstants.TABLE_CREATE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database Open");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DataBaseConstants.TABLE_NAME);
        onCreate(db);
        Log.i(AppGlobals.getLogTag(getClass()), "Database Open");
    }

    public void addNewLocation(String columnName, String value) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, value);
        sqLiteDatabase.insert(DataBaseConstants.TABLE_NAME, null, contentValues);
        dispatchEventOnNewEntryCreated();
        sqLiteDatabase.close();
        System.out.println("DBBDBDBD");
    }

    public void setOnDatabaseChangedListener(OnDatabaseChangedListener listener) {
        mListeners.add(listener);
    }

    private void dispatchEventOnNewEntryCreated() {
        for (OnDatabaseChangedListener listener : mListeners) {
            listener.onNewEntryCreated();
        }
    }

    public interface OnDatabaseChangedListener {
        void onNewEntryCreated();
    }
}
