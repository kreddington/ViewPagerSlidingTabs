/*
 * Copyright (C) 2015 Cereus Women and Kim Reddington
 */
package com.cereuswomen.marketingmessage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cereuswomen.marketingmessage.data.DataContract.MessageEntry;
import com.cereuswomen.marketingmessage.data.DataContract.UserEntry;

/**
 * Manages a local database for the marketing message data elements.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "message.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Creates the Database */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold user registration and login data.
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                UserEntry.COLUMN_DATE_REGISTERED + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_DATE_LAST_ACCESSED + " INTEGER NOT NULL" +
                " );";

        final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " + MessageEntry.TABLE_NAME + " (" +

                MessageEntry._ID + " INTEGER PRIMARY KEY, " +

                // the ID of the USER entry associated with this marketing message data
                MessageEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                MessageEntry.COLUMN_LAST_SCREEN + " INTEGER NOT NULL, " +
                MessageEntry.COLUMN_TARGET_MARKET + " TEXT, " +
                MessageEntry.COLUMN_ULTIMATE_PROBLEM + " TEXT, " +

                MessageEntry.COLUMN_ULTIMATE_RESULT + " TEXT, " +
                MessageEntry.COLUMN_PROBLEM1 + " TEXT, " +
                MessageEntry.COLUMN_PROBLEM2 + " TEXT, " +
                MessageEntry.COLUMN_PROBLEM3 + " TEXT, " +
                MessageEntry.COLUMN_PROBLEM4 + " TEXT, " +
                MessageEntry.COLUMN_PROBLEM5 + " TEXT, " +
                MessageEntry.COLUMN_RESULT1 + " TEXT, " +
                MessageEntry.COLUMN_RESULT2 + " TEXT, " +
                MessageEntry.COLUMN_RESULT3 + " TEXT, " +
                MessageEntry.COLUMN_RESULT4 + " TEXT, " +
                MessageEntry.COLUMN_RESULT5 + " TEXT, " +
                MessageEntry.COLUMN_FIRST_HELPING_VERB + " TEXT, " +
                MessageEntry.COLUMN_SECOND_HELPING_VERB + " TEXT, " +
                MessageEntry.COLUMN_FULL_MESSAGE + " TEXT, " +
                MessageEntry.COLUMN_DATE_CREATED + " INTEGER NOT NULL, " +
                MessageEntry.COLUMN_DATE_COMPLETED + " INTEGER, " +

                // Set up the USER ID column as a foreign key to message table.
                " FOREIGN KEY (" + MessageEntry.COLUMN_USER_ID + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                // To assure the application has just one message entry per user
                // it's created a UNIQUE constraint with REPLACE strategy
                // TODO: Remove in future if the feature to add multiple messages per user is activated
                " UNIQUE (" + MessageEntry.COLUMN_USER_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MESSAGE_TABLE);
    }

    /** Wipes out the current database and creates a new one. Used when database version has changes. */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
