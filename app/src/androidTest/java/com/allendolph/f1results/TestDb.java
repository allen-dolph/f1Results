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
        ContentValues driverValues = getDriverContentValues();

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
        ContentValues constructorValues = getConstructorContentValues();

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
        ContentValues circuitValues = getCircuitContentValues();

        long circuitRowId;
        circuitRowId = db.insert(CircuitEntry.TABLE_NAME, null, circuitValues);

        // Verify we got a row back
        assertTrue(circuitRowId != -1);
        Log.d(LOG_TAG, "new circuit row id: " + circuitRowId);

        // Now query the circuit table and get a cursor back
        Cursor circuitCursor = simpleQuery(db, CircuitEntry.TABLE_NAME);

        // Use our helper method to validate
        validateCursor(circuitValues, circuitCursor);
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
    ContentValues getDriverContentValues() {
        ContentValues values = new ContentValues();

        String driverId = "hamilton";
        int permanentNumber = 44;
        String code = "HAM";
        String url = "http://testurl.com";
        String givenName = "Lewis";
        String familyName = "Hamilton";
        String dateOfBirth = "1985-01-07";
        String nationality = "British";

        values.put(DriverEntry.COLUMN_DRIVER_ID, driverId);
        values.put(DriverEntry.COLUMN_PERMANENT_NUMBER, permanentNumber);
        values.put(DriverEntry.COLUMN_CODE, code);
        values.put(DriverEntry.COLUMN_URL, url);
        values.put(DriverEntry.COLUMN_GIVEN_NAME, givenName);
        values.put(DriverEntry.COLUMN_FAMILY_NAME, familyName);
        values.put(DriverEntry.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        values.put(DriverEntry.COLUMN_NATIONALITY, nationality);

        return values;
    }

    ContentValues getConstructorContentValues() {
        ContentValues values = new ContentValues();

        String constructorId = "mclaren";
        String url = "http://test.com";
        String name = "McLaren";
        String nationality = "British";

        values.put(ConstructorEntry.COLUMN_CONSTRUCTOR_ID, constructorId);
        values.put(ConstructorEntry.COLUMN_URL, url);
        values.put(ConstructorEntry.COLUMN_NAME, name);
        values.put(ConstructorEntry.COLUMN_NATIONALITY, nationality);

        return values;
    }

    ContentValues getCircuitContentValues() {
        ContentValues values = new ContentValues();

        String circuitId = "albert_park";
        String url = "http://test.circuit.com";
        String circuitName = "Albert Park Grand Prix Circuit";
        double locationLat = -37.8497;
        double locationLong = 144.968;
        String locality = "Melbourne";
        String country = "Australia";

        values.put(CircuitEntry.COLUMN_CIRCUIT_ID, circuitId);
        values.put(CircuitEntry.COLUMN_URL, url);
        values.put(CircuitEntry.COLUMN_CIRCUIT_NAME, circuitName);
        values.put(CircuitEntry.COLUMN_LOCATION_LAT, locationLat);
        values.put(CircuitEntry.COLUMN_LOCATION_LONG, locationLong);
        values.put(CircuitEntry.COLUMN_LOCATION_LOCALITY, locality);
        values.put(CircuitEntry.COLUMN_LOCATION_COUNTRY, country);

        return values;
    }
}
