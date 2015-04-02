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
        ContentValues raceValues = getRaceContentValues(circuitRowId);

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
                getResultContentValues(driverRowId, constructorRowId, raceRowId);

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

    // Helper methods to get values to insert for the tests
    ContentValues getRaceContentValues(long circuitId) {
        ContentValues values = new ContentValues();

        int season = 2008;
        int round = 1;
        String url = "http://test.url";
        String raceName = "Australian Grand Prix";
        String date = "2008-03-16";
        String time = "04:30:00Z";

        values.put(RaceEntry.COLUMN_CIRCUIT_ID, circuitId);

        values.put(RaceEntry.COLUMN_SEASON, season);
        values.put(RaceEntry.COLUMN_ROUND, round);
        values.put(RaceEntry.COLUMN_URL, url);
        values.put(RaceEntry.COLUMN_RACE_NAME, raceName);
        values.put(RaceEntry.COLUMN_DATE, date);
        values.put(RaceEntry.COLUMN_TIME, time);

        return values;
    }

    ContentValues getResultContentValues(long driverId, long constructorId, long raceId) {
        ContentValues values = new ContentValues();

        int number = 22;
        int position = 1;
        String positionText = "1";
        int points = 10;
        int grid = 1;
        int laps = 58;
        String status = "Finished";
        String millis = "5690616";
        String time = "1:34:50:616";
        int fastestLapRank = 2;
        int fastestLapLap = 39;
        String fastestLapTime = "1:27:452";
        String averageSpeedUnits = "kph";
        double averageSpeedSpeed = 218.300;

        values.put(ResultsEntry.COLUMN_DRIVER_ID, driverId);
        values.put(ResultsEntry.COLUMN_CONSTRUCTOR_ID, constructorId);
        values.put(ResultsEntry.COLUMN_RACE_ID, raceId);

        values.put(ResultsEntry.COLUMN_NUMBER, number);
        values.put(ResultsEntry.COLUMN_POSITION, position);
        values.put(ResultsEntry.COLUMN_POSITION_TEXT, positionText);
        values.put(ResultsEntry.COLUMN_POINTS, points);
        values.put(ResultsEntry.COLUMN_GRID, grid);
        values.put(ResultsEntry.COLUMN_LAPS, laps);
        values.put(ResultsEntry.COLUMN_STATUS, status);
        values.put(ResultsEntry.COLUMN_TIME_MILLIS, millis);
        values.put(ResultsEntry.COLUMN_TIME, time);
        values.put(ResultsEntry.COLUMN_FASTEST_LAP_RANK, fastestLapRank);
        values.put(ResultsEntry.COLUMN_FASTEST_LAP_LAP, fastestLapLap);
        values.put(ResultsEntry.COLUMN_FASTEST_LAP_TIME, fastestLapTime);
        values.put(ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_UNITS, averageSpeedUnits);
        values.put(ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_SPEED, averageSpeedSpeed);

        return values;
    }
}
