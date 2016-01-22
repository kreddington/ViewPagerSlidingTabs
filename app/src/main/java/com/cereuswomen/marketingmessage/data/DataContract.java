/*
 * Copyright (c) 2015. Cereus Women and Kim Reddington. All rights reserved worldwide.
 */

package com.cereuswomen.marketingmessage.data;

/**
 * Created by Kim on 9/20/2015.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the message database.
 */
public class DataContract {
    // The "Content authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.cereuswomen.marketingmessage";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths of databases (appended to base content URI for possible URI's)
    // ie. content://com.cereuswomen.marketingmessage/user
    public static final String PATH_USER = "user";
    public static final String PATH_MESSAGE = "message";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long date) {
        // normalize dates to the beginning of the (UTC) day
        Time time = new Time();
        time.set(date);
        int julianDay = Time.getJulianDay(date, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the user table */
    public static final class UserEntry implements BaseColumns {

        // Used for ContentProvider
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        // Used for ContentResolver
        // There should never be more than one user entry to return.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = PATH_USER;

        // Columns defined inside table
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_DATE_REGISTERED = "date_registered";
        public static final String COLUMN_DATE_LAST_ACCESSED = "date_last_accessed";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the marketing message table */
    public static final class MessageEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE).build();

        // CURSOR_DIR_BASE_TYPE included for future when more than one message may be required per user,
        // or if no message has yet been stored for particular user.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MESSAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MESSAGE;

        // Table name
        public static final String TABLE_NAME = PATH_MESSAGE;

        // Columns defined inside table
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_LAST_SCREEN = "last_screen";
        public static final String COLUMN_TARGET_MARKET = "target_market";
        public static final String COLUMN_ULTIMATE_PROBLEM = "ultimate_problem";
        public static final String COLUMN_ULTIMATE_RESULT = "ultimate_result";
        public static final String COLUMN_PROBLEM1 = "problem1";
        public static final String COLUMN_PROBLEM2 = "problem2";
        public static final String COLUMN_PROBLEM3 = "problem3";
        public static final String COLUMN_PROBLEM4 = "problem4";
        public static final String COLUMN_PROBLEM5 = "problem5";
        public static final String COLUMN_RESULT1 = "result1";
        public static final String COLUMN_RESULT2 = "result2";
        public static final String COLUMN_RESULT3 = "result3";
        public static final String COLUMN_RESULT4 = "result4";
        public static final String COLUMN_RESULT5 = "result5";
        public static final String COLUMN_FIRST_HELPING_VERB = "first_helping_verb";
        public static final String COLUMN_SECOND_HELPING_VERB = "second_helping_verb";
        public static final String COLUMN_FULL_MESSAGE = "full_message";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_DATE_COMPLETED = "date_completed";

        public static Uri buildMessageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Gets the user based on the URI to be used in retrieving the appropriate message
        public static String getUserFromUri(Uri uri) {
            //TODO: added indexoutofboundsexception in case user id is not available. probably means there is no user and dbs are empty.
            //TODO: double check that this is necessary after real use cases.
            String userRowId;
            try {
                userRowId = uri.getPathSegments().get(1);
            } catch (IndexOutOfBoundsException ex) {
                userRowId = null;
            }

            return userRowId;

        }

    }
}
