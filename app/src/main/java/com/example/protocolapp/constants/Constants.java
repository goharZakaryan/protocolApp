package com.example.protocolapp.constants;

public class Constants {
    // database or db name
    public static final String DATABASE_NAME = "USER_DB";
    //database version
    public static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE_NAME = "USER_TABLE";
    public static final String USER_ID = "ID";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "PASSWORD";


    // query for create table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_EMAIL + " TEXT, "
            + USER_PASSWORD + " TEXT"
            + " );";

}