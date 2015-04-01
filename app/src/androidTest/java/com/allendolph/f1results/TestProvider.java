package com.allendolph.f1results;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.allendolph.f1results.data.F1Contract.DriverEntry;
import com.allendolph.f1results.data.F1Contract.ConstructorEntry;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;
import com.allendolph.f1results.data.F1Contract.ResultsEntry;
import com.allendolph.f1results.data.F1DbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by allendolph on 3/31/15.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = "TEST_PROVIDER";

    public void testInsertReadUpdateDeleteProvider() {
        testDeleteAllRecords();

        // create a map of driver values
        ContentValues driverValues = TestUtil.getDriverContentValues();

        long driverRowId;
        Uri driverUri = mContext.getContentResolver().insert(DriverEntry.CONTENT_URI, driverValues);
        driverRowId = ContentUris.parseId(driverUri);

        // Verify we got a row back
        assertTrue(driverRowId != -1);
        Log.d(LOG_TAG, "new driver row id: " + driverRowId);

        Cursor driverCursor = simpleQuery(DriverEntry.buildDriverUri(driverRowId));

        // Use our helper method to test the driver entry
        validateCursor(driverValues, driverCursor);
    }

    public void testDeleteAllRecords() {
        simpleDelete(DriverEntry.CONTENT_URI);
        simpleDelete(ConstructorEntry.CONTENT_URI);
        simpleDelete(CircuitEntry.CONTENT_URI);

        Cursor driverCursor = simpleQuery(DriverEntry.CONTENT_URI);
        Cursor constructorCursor = simpleQuery(ConstructorEntry.CONTENT_URI);
        Cursor circuitCursor = simpleQuery(CircuitEntry.CONTENT_URI);

        assertEquals(driverCursor.getCount(), 0);
        assertEquals(constructorCursor.getCount(), 0);
        assertEquals(circuitCursor.getCount(), 0);
    }

    // Make sure that everything in our content matches our insert
    static public void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        if(valueCursor.moveToFirst()) {
            for (Map.Entry<String, Object> entry : valueSet) {
                String columnName = entry.getKey();
                int idx = valueCursor.getColumnIndex(columnName);
                assertFalse(-1 == idx);

                String expectedValue = entry.getValue().toString();
                assertEquals(expectedValue, valueCursor.getString(idx));
            }
        } else {
            // no rows returned, something went wrong
            fail();
        }
    }

    private void simpleDelete(Uri uri) {
        mContext.getContentResolver().delete(uri, null, null);
    }

    private Cursor simpleQuery(Uri uri) {
        return mContext.getContentResolver().query(uri, null, null, null, null);
    }
}
