package com.allendolph.f1results;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.allendolph.f1results.data.F1Contract;
import com.allendolph.f1results.data.F1DbHelper;
import com.allendolph.f1results.data.F1Contract.DriverEntry;
import com.allendolph.f1results.data.F1Contract.ConstructorEntry;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;
import com.allendolph.f1results.data.F1Contract.ResultsEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by allendolph on 3/31/15.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = "TEST_DB";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(F1DbHelper.DATABASE_NAME);
        SQLiteDatabase db = new F1DbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        mContext.deleteDatabase(F1DbHelper.DATABASE_NAME);
        F1DbHelper dbHelper = new F1DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Test Drivers Table
        // create a map of values to test driver entry
        ContentValues driverValues = TestUtil.getDriverContentValues();

        long driverRowId;
        driverRowId = db.insert(DriverEntry.TABLE_NAME, null, driverValues);

        // Verify we got a row back
        assertTrue(driverRowId != -1);
        Log.d(LOG_TAG, "new driver row id: " + driverRowId);

        // Now query the drivers table and get a cursor back
        Cursor driverCursor = simpleQuery(db, DriverEntry.TABLE_NAME);

        // Use our helper method to validate
        validateCursor(driverValues, driverCursor);

        // Test Constructors Table
        // create a map of constructor values
        ContentValues constructorValues = TestUtil.getConstructorContentValues();

        long constructorRowId;
        constructorRowId = db.insert(ConstructorEntry.TABLE_NAME, null, constructorValues);

        // Verify we got a row back
        assertTrue(constructorRowId != -1);
        Log.d(LOG_TAG, "new constructor row id: " + constructorRowId);

        // Now query the constructors table and get a cursor back
        Cursor constructorCursor = simpleQuery(db, ConstructorEntry.TABLE_NAME);

        // Use our helper method to validate
        validateCursor(constructorValues, constructorCursor);

        // Test Circuit table
        // create a map of circuit values
        ContentValues circuitValues = TestUtil.getCircuitContentValues();

        long circuitRowId;
        circuitRowId = db.insert(CircuitEntry.TABLE_NAME, null, circuitValues);

        // Verify we got a row back
        assertTrue(circuitRowId != -1);
        Log.d(LOG_TAG, "new circuit row id: " + circuitRowId);

        // Now query the circuit table and get a cursor back
        Cursor circuitCursor = simpleQuery(db, CircuitEntry.TABLE_NAME);

        // Use our helper method to validate
        validateCursor(circuitValues, circuitCursor);

        // Test the Race Table
        // create a map for race values
        ContentValues raceValues = TestUtil.getRaceContentValues(circuitRowId);

        long raceRowId;
        raceRowId = db.insert(RaceEntry.TABLE_NAME, null, raceValues);

        // Verify we got a row back
        assertTrue(raceRowId != -1);
        Log.d(LOG_TAG, "new race row id: " + raceRowId);

        // Now query the race table and get a cursor back
        Cursor raceCursor = simpleQuery(db, RaceEntry.TABLE_NAME);

        // Use our helper method to validate
        validateCursor(raceValues, raceCursor);

        // Test Results table
        ContentValues resultValues =
                TestUtil.getResultContentValues(driverRowId, constructorRowId, raceRowId);

        long resultRowId;
        resultRowId = db.insert(ResultsEntry.TABLE_NAME, null, resultValues);

        // Verify we got a row back
        assertTrue(raceRowId != -1);
        Log.d(LOG_TAG, "new result row id: " + raceRowId);

        // Now query the result table and get a cursor back
        Cursor resultsCursor = simpleQuery(db, ResultsEntry.TABLE_NAME);

        // use our helper method to validate
        validateCursor(resultValues, resultsCursor);
    }

    // Helper Methods for easier validation
    static public Cursor simpleQuery(SQLiteDatabase db, String tableName) {
        return db.query(
                tableName, // Table to query
                null, // columns to return -> null for all
                null, // Columns for the where clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // Sort order
        );
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
}
