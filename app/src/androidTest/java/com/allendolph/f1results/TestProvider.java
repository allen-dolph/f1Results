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

        // create a map of constructor values
        ContentValues constructorValues = TestUtil.getConstructorContentValues();

        long constructorRowId;
        Uri constructorUri = mContext.getContentResolver()
                .insert(ConstructorEntry.CONTENT_URI, constructorValues);
        constructorRowId = ContentUris.parseId(constructorUri);

        // Verify we got a row back
        assertTrue(constructorRowId != -1);
        Log.d(LOG_TAG, "new constructor row id: " + constructorRowId);

        Cursor constructorCursor
                = simpleQuery(ConstructorEntry.buildConstructorUri(constructorRowId));

        // Use our helper method to test the constructor entry
        validateCursor(constructorValues, constructorCursor);

        // create a map of circuit values
        ContentValues circuitValues = TestUtil.getCircuitContentValues();

        long circuitRowId;
        Uri circuitUri = mContext.getContentResolver()
                .insert(CircuitEntry.CONTENT_URI, circuitValues);
        circuitRowId = ContentUris.parseId(circuitUri);

        // Verify we got a row back
        assertTrue(circuitRowId != -1);
        Log.d(LOG_TAG, "new circuit row id: " + circuitRowId);

        Cursor circuitCursor = simpleQuery(CircuitEntry.buildCircuitUri(circuitRowId));

        // Use our helper method to test the circuit entry
        validateCursor(circuitValues, circuitCursor);
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

    public void testGetType() {
        // DRIVER
        // content://com.allendolph.f1results/driver
        String type = mContext.getContentResolver().getType(DriverEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.allendolph.f1results/driver
        assertEquals(DriverEntry.CONTENT_TYPE, type);

        long testDriverId = 1;
        // content://com.allendolph.f1results/driver/1
        type = mContext.getContentResolver().getType(DriverEntry.buildDriverUri(testDriverId));
        // vnd.android.cursor.dir/com.allendolph.f1results/driver/1
        assertEquals(DriverEntry.CONTENT_ITEM_TYPE, type);

        // CONSTRUCTOR
        // content://com.allendolph.f1results/constructor
        type = mContext.getContentResolver().getType(ConstructorEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.allendolph.f1results/constructor
        assertEquals(ConstructorEntry.CONTENT_TYPE, type);

        long testConstructorId = 99;
        // content://com.allendolph.f1results/constructor/99
        type = mContext.getContentResolver().getType(ConstructorEntry.buildConstructorUri(99));
        // vnd.android.cursor.dir/com.allendolph.f1results/constructor/99
        assertEquals(ConstructorEntry.CONTENT_ITEM_TYPE, type);

        // CIRCUIT
        // content://com.allendolph.f1results/circuit
        type = mContext.getContentResolver().getType(CircuitEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.allendolph.f1results/circuit
        assertEquals(CircuitEntry.CONTENT_TYPE, type);

        long testCircuitId = 5;
        // content://com.allendolph.f1results/circuit/5
        type = mContext.getContentResolver().getType(CircuitEntry.buildCircuitUri(testCircuitId));
        // vnd.android.cursor.dir/com.allendolph.f1results/circuit/5
        assertEquals(CircuitEntry.CONTENT_ITEM_TYPE, type);
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
