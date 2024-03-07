package com.example.protocolapp.constants;

public class ProtocolConstants {
    public static final String DATABASE_NAME = "PROTOCOL_DB";
    //database version
    public static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE_NAME = "PROTOCOL_TABLE";
    public static final String PROTOCOL_ID = "ID";
    public static final String PROTOCOL_NAME = "NAME";
    public static final String PROTOCOL_TASKLIST = "TASKLIST";
    public static final String PROTOCOL_TASKLISTAUTHOR = "TASKLISTAUTHOR";


    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + PROTOCOL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PROTOCOL_NAME + " TEXT, "
            + PROTOCOL_TASKLIST + " TEXT, "
            + PROTOCOL_TASKLISTAUTHOR + " TEXT"
            + " );";
}
