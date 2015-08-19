package com.byteshaft.kidmonitor.database;


public class LocationDataBaseConstants {

    public static final String DATABASE_NAME = "Location.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "LocationDatabase";
    public static final String UPLOAD_LOCATION_COLUMN = "TO_BE_UPLOADED_LOCATION";
    public static final String DELETE_LOCATION_COLUMN = "TO_BE_DELETED_LOCATION";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String CREATE_LOCATION_TABLE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UPLOAD_LOCATION_COLUMN + " TEXT,"
            + DELETE_LOCATION_COLUMN + " TEXT,"
            + GlobalConstants.TIME_STAMP_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
