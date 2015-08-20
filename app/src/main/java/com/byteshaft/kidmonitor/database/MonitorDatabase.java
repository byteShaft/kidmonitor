package com.byteshaft.kidmonitor.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.Constants.DatabaseConstants;
import com.byteshaft.kidmonitor.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class MonitorDatabase extends SQLiteOpenHelper {

    private ArrayList<OnDatabaseChangedListener> mListeners = new ArrayList<>();

    public MonitorDatabase(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String dataType, String uri, String timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.DATA_TYPE_COLUMN, dataType);
        values.put(DatabaseConstants.URI_COLUMN, uri);
        values.put(DatabaseConstants.TIME_STAMP_COLUMN, timestamp);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
        Helpers.checkInternetAndUploadPendingData();
    }

    public ArrayList<HashMap> getAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String dataType = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.DATA_TYPE_COLUMN));
            String uri = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.URI_COLUMN));
            String time = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.TIME_STAMP_COLUMN));
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("unique_id", String.valueOf(unique_id));
            hashMap.put("data_type", dataType);
            hashMap.put("uri", uri);
            hashMap.put("time_stamp", time);
            list.add(hashMap);
        }
        db.close();
        cursor.close();
        return list;
    }

    public void deleteEntry(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM "
                + DatabaseConstants.TABLE_NAME
                + " WHERE "
                + DatabaseConstants.ID_COLUMN
                + "="
                + ID;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        db.close();
        cursor.close();
    }

    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + DatabaseConstants.TABLE_NAME;
        db.execSQL(query);
        db.close();
    }

    public boolean isEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseConstants.TABLE_NAME, null);
        boolean isEmpty;
        isEmpty = !cursor.moveToNext();
        cursor.close();
        return isEmpty;
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
