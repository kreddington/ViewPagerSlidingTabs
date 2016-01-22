/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cereuswomen.marketingmessage.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_USER_DIR = DataContract.UserEntry.CONTENT_URI;
    //private static final Uri TEST_WEATHER_WITH_LOCATION_DIR = WeatherContract.WeatherEntry.buildWeatherLocation(LOCATION_QUERY);

    private static final Uri TEST_MESSAGE_BY_USER_DIR = DataContract.MessageEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MessageProvider.buildUriMatcher();

        assertEquals("Error: The USER URI was matched incorrectly.",
                testMatcher.match(TEST_USER_DIR), MessageProvider.USER);
        assertEquals("Error: The MESSAGE_BY_USER URI was matched incorrectly.",
                testMatcher.match(TEST_MESSAGE_BY_USER_DIR), MessageProvider.MESSAGE_BY_USER);
    }
}
