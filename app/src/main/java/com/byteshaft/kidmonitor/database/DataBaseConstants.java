package com.byteshaft.kidmonitor.database;


public class DataBaseConstants {

    public static final String DATABASE_NAME = "KidmoniterDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "KidmoniterDatabase";
    public static final String UPLOAD_LOCATION_COLUMN = "TO_BE_UPLOADED_LOCATION";
    public static final String DELETE_LOCATION_COLUMN = "TO_BE_DELETED_LOCATION";
    public static final String UPLOAD_CALL_RECORDING = "TO_BE_UPLOADED_CALL_REC";
    public static final String DELETE_CALL_RECORDING = "TO_BE_DELETE_CALL_REC";
    public static final String UPLOAD_VIDEO_RECORDING = "TO_BE_UPLOADED_VIDEO_REC";
    public static final String DELETE_VIDEO_RECORDING = "TO_BE_DELETED_VIDEO_REC";
    public static final String TIME_STAMP_COLUMN = "TIME";
    public static final String USER_ID_COLUMN = "USER_ID";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UPLOAD_LOCATION_COLUMN + " TEXT,"
            + DELETE_LOCATION_COLUMN + " TEXT,"
            + UPLOAD_CALL_RECORDING + " TEXT,"
            + DELETE_CALL_RECORDING + " TEXT,"
            + UPLOAD_VIDEO_RECORDING + " TEXT,"
            + DELETE_VIDEO_RECORDING + " TEXT,"
            + TIME_STAMP_COLUMN + " TEXT,"
            + USER_ID_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
