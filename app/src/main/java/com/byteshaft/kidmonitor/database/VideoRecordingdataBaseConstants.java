package com.byteshaft.kidmonitor.database;


public class VideoRecordingdataBaseConstants {

    public static final String DATABASE_NAME = "VideoRecording.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "VideoRecordingDatabase";
    public static final String UPLOAD_VIDEO_RECORDING = "TO_BE_UPLOADED_VIDEO_REC";
    public static final String DELETE_VIDEO_RECORDING = "TO_BE_DELETED_VIDEO_REC";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UPLOAD_VIDEO_RECORDING + " TEXT,"
            + DELETE_VIDEO_RECORDING + " TEXT,"
            + GlobalConstants.TIME_STAMP_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
