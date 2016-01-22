/*
 * Copyright (c) 2015. Cereus Women and Kim Reddington. All rights reserved worldwide.
 */
package com.cereuswomen.marketingmessage.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MessageProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    static final int USER = 100;
    static final int MESSAGE_BY_USER = 200;

    //message.user_id = ?
    private static final String sMessageSelection = DataContract.MessageEntry.COLUMN_USER_ID + " = ? ";
//            DataContract.MessageEntry.TABLE_NAME +
//                    "." +


    /** Returns the Marketing Message data for the particular user logged in.
     *
     * @param uri of UserEntry
     * @param projection - columns to return with cursor
     * @param sortOrder - ability in future to store multiple marketing messages
     * @return cursor containing message data for user
     */
    private Cursor getMessageByUser(Uri uri, String[] projection, String sortOrder) {
        String user = String.valueOf(DataContract.MessageEntry.getUserFromUri(uri));
        String selection = null;
        String[] selectionArgs;

        if (user != null) {
            selection = sMessageSelection;
            selectionArgs = new String[]{user};
        } else {
            // selection and selectionArgs should stay null.
            selectionArgs = null;
        }



        return mOpenHelper.getReadableDatabase().query(
                DataContract.MessageEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataContract.PATH_USER, USER);
        matcher.addURI(authority, DataContract.PATH_MESSAGE, MESSAGE_BY_USER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MESSAGE_BY_USER:
                // could return zero or multiple entries.
                return DataContract.MessageEntry.CONTENT_TYPE;
            case USER:
                // will always return one entry.
                return DataContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "message/*"
            case MESSAGE_BY_USER: {
                retCursor = getMessageByUser(uri, projection, sortOrder);
                break;
            }

            // "user"
            case USER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case USER: {
                normalizeUserDates(values);
                long _id = db.insert(DataContract.UserEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MESSAGE_BY_USER: {
                normalizeMessageDates(values);
                long _id = db.insert(DataContract.MessageEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.MessageEntry.buildMessageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private void normalizeUserDates(ContentValues values) {
        // normalize the date value
        if (values.containsKey(DataContract.UserEntry.COLUMN_DATE_REGISTERED)) {
            long dateValue = values.getAsLong(DataContract.UserEntry.COLUMN_DATE_REGISTERED);
            values.put(DataContract.UserEntry.COLUMN_DATE_REGISTERED, DataContract.normalizeDate(dateValue));
        }
        if (values.containsKey(DataContract.UserEntry.COLUMN_DATE_LAST_ACCESSED)) {
            long dateValue = values.getAsLong(DataContract.UserEntry.COLUMN_DATE_LAST_ACCESSED);
            values.put(DataContract.UserEntry.COLUMN_DATE_LAST_ACCESSED, DataContract.normalizeDate(dateValue));
        }
    }

    private void normalizeMessageDates(ContentValues values) {
        if (values.containsKey(DataContract.MessageEntry.COLUMN_DATE_CREATED)) {
            long dateValue = values.getAsLong(DataContract.MessageEntry.COLUMN_DATE_CREATED);
            values.put(DataContract.MessageEntry.COLUMN_DATE_CREATED, DataContract.normalizeDate(dateValue));
        }
        if (values.containsKey(DataContract.MessageEntry.COLUMN_DATE_COMPLETED)) {
            long dateValue = values.getAsLong(DataContract.MessageEntry.COLUMN_DATE_COMPLETED);
            values.put(DataContract.MessageEntry.COLUMN_DATE_COMPLETED, DataContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case USER:
                rowsDeleted = db.delete(
                        DataContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MESSAGE_BY_USER:
                rowsDeleted = db.delete(
                        DataContract.MessageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER:
                normalizeUserDates(values);
                rowsUpdated = db.update(DataContract.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MESSAGE_BY_USER:
                rowsUpdated = db.update(DataContract.MessageEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}