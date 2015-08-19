package com.byteshaft.kidmonitor.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.utils.Helpers;

import java.util.ArrayList;

public class DataBaseHelpers extends SQLiteOpenHelper {

    private ArrayList<OnDatabaseChangedListener> mListeners = new ArrayList<>();

    public DataBaseHelpers(Context context) {
        super(context, LocationDataBaseConstants.DATABASE_NAME, null, LocationDataBaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LocationDataBaseConstants.CREATE_LOCATION_TABLE);
        db.execSQL(VideoRecordingdataBaseConstants.CREATE_VIDEO_RECORDING_TABLE);
        db.execSQL(CallRecordingDataBaseConstants.CREATE_CALL_RECORDING_TABLE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database Open");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + LocationDataBaseConstants.TABLE_NAME);
        onCreate(db);
        Log.i(AppGlobals.getLogTag(getClass()), "Database Open");
    }

    public void newEntryToDatabase(String columnName, String value, String tableName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, value);
        contentValues.put(GlobalConstants.TIME_STAMP_COLUMN, Helpers.getCurrentDateandTime());
        sqLiteDatabase.insert(tableName, null, contentValues);
        dispatchEventOnNewEntryCreated();
        sqLiteDatabase.close();
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
