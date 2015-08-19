package com.byteshaft.kidmonitor.database;


public class CallRecordingDataBaseConstants {

    public static final String DATABASE_NAME = "CallRecording.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "CallRecordingDatabase";
    public static final String UPLOAD_CALL_RECORDING = "TO_BE_UPLOADED_CALL_REC";
    public static final String DELETE_CALL_RECORDING = "TO_BE_DELETE_CALL_REC";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UPLOAD_CALL_RECORDING + " TEXT,"
            + DELETE_CALL_RECORDING + " TEXT,"
            + GlobalConstants.TIME_STAMP_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
