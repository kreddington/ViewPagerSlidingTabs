package com.cereuswomen.marketingmessage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.cereuswomen.marketingmessage.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createMessageValues(long userRowId) {
        ContentValues messageValues = new ContentValues();

        //Attach the user row ID to the URI, so we can access it later.
        DataContract.UserEntry.buildUserUri(userRowId);

        messageValues.put(DataContract.MessageEntry.COLUMN_USER_ID, userRowId);
        messageValues.put(DataContract.MessageEntry.COLUMN_DATE_CREATED, TEST_DATE);
        messageValues.put(DataContract.MessageEntry.COLUMN_DATE_COMPLETED, TEST_DATE);
        messageValues.put(DataContract.MessageEntry.COLUMN_TARGET_MARKET, "women entrepreneurs");
        messageValues.put(DataContract.MessageEntry.COLUMN_PROBLEM1, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_PROBLEM2, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_PROBLEM3, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_PROBLEM4, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_PROBLEM5, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_RESULT1, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_RESULT2, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_RESULT3, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_RESULT4, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_RESULT5, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_LAST_SCREEN, 1);
        messageValues.put(DataContract.MessageEntry.COLUMN_ULTIMATE_PROBLEM, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_ULTIMATE_RESULT, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_FIRST_HELPING_VERB, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_SECOND_HELPING_VERB, "");
        messageValues.put(DataContract.MessageEntry.COLUMN_FULL_MESSAGE, "");

        return messageValues;
    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the WeatherContract.
     */
    static ContentValues createUserValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.UserEntry.COLUMN_EMAIL, "kim@cereuswomen.com");
        testValues.put(DataContract.UserEntry.COLUMN_FIRST_NAME, "Kim");
        testValues.put(DataContract.UserEntry.COLUMN_LAST_NAME, "Reddington");
        testValues.put(DataContract.UserEntry.COLUMN_PASSWORD, "reddington");
        testValues.put(DataContract.UserEntry.COLUMN_DATE_REGISTERED, 1419033600L);  // December 20th, 2014
        testValues.put(DataContract.UserEntry.COLUMN_DATE_LAST_ACCESSED, 1419033600L);  // December 20th, 2014

        return testValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
    static long insertUserValues(Context context) {
        // insert our test records into the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createUserValues();

        long userRowId;
        userRowId = db.insert(DataContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert User Values", userRowId != -1);

        return userRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
