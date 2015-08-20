package com.byteshaft.kidmonitor.Constants;

public class DatabaseConstants {

    public static final String DATABASE_NAME = "MonitorDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "MonitorDatabase";
    public static final String URI_COLUMN = "URI";
    public static final String TIME_STAMP_COLUMN = "TIME";
    public static final String DATA_TYPE_COLUMN = "DATA_TYPE";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + URI_COLUMN + " TEXT,"
            + TIME_STAMP_COLUMN + " TEXT,"
            + DATA_TYPE_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
