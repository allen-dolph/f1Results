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

import junit.framework.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by allendolph on 3/31/15.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = "TEST_PROVIDER";

    public void testInsertReadUpdateDeleteProvider() {
        testDeleteAllRecords();

        // DRIVER
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

        // CONSTRUCTOR
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

        // CIRCUIT
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

        // RACE
        // create a map of race values
        ContentValues raceValues = TestUtil.getRaceContentValues(circuitRowId);

        String raceSeason;
        String raceRound;
        Uri raceUri = mContext.getContentResolver().insert(RaceEntry.CONTENT_URI, raceValues);
        raceSeason = RaceEntry.getSeasonFromUri(raceUri);
        raceRound = RaceEntry.getRoundFromUri(raceUri);

        // Verify we got a row back
        assertEquals(raceSeason, String.valueOf(TestUtil.RACE_SEASON));
        assertEquals(raceRound, String.valueOf(TestUtil.RACE_ROUND));

        // now test our race query methods
        // /race
        Cursor raceCursor = simpleQuery(RaceEntry.CONTENT_URI);
        validateCursor(raceValues, raceCursor);
        // we need the race row id for later tests
        raceCursor.moveToFirst();
        long raceRowId = raceCursor.getLong(raceCursor.getColumnIndex(RaceEntry._ID));
        raceCursor.close();

        // /race/2008
        raceCursor = simpleQuery(RaceEntry.buildRaceSeasonUri(String.valueOf(TestUtil.RACE_SEASON)));
        validateCursor(raceValues, raceCursor);
        raceCursor.close();

        // /race/2008/5
        Uri raceWithSeasonAndRoundUri = RaceEntry.buildRaceSeasonWithRound(
                String.valueOf(TestUtil.RACE_SEASON),
                String.valueOf(TestUtil.RACE_ROUND));
        raceCursor = simpleQuery(raceWithSeasonAndRoundUri);
        ContentValues raceWithCircuitValues = new ContentValues();

        // combine the race and circuit values as that is what we are expecting back
        raceWithCircuitValues.putAll(raceValues);
        raceWithCircuitValues.putAll(circuitValues);
        validateCursor(raceWithCircuitValues, raceCursor);

        //RESULT
        // create a map of result values
        ContentValues resultValues = TestUtil.getResultContentValues(
                driverRowId, constructorRowId, raceRowId);

        String season;
        String round;
        String position;
        Uri resultUri = mContext.getContentResolver().insert(ResultsEntry.CONTENT_URI, resultValues);

        // verify we got a row back
        season = ResultsEntry.getSeasonFromUri(resultUri);
        round = ResultsEntry.getRoundFromUri(resultUri);
        position = ResultsEntry.getPositionFromUri(resultUri);

        assertEquals(season, String.valueOf(TestUtil.RACE_SEASON));
        assertEquals(round, String.valueOf(TestUtil.RACE_ROUND));
        assertEquals(position, String.valueOf(TestUtil.RESULT_POSITION));

        // now test our result query uris
        // result
        Cursor resultCursor = simpleQuery(ResultsEntry.CONTENT_URI);
        validateCursor(resultValues, resultCursor);
        resultCursor.close();

        // result/2008/5
        Uri resultWithSeasonAndRoundUri = ResultsEntry.buildResultWithSeasonAndRoundUri(
                String.valueOf(TestUtil.RACE_SEASON),
                String.valueOf(TestUtil.RACE_ROUND));
        resultCursor = simpleQuery(resultWithSeasonAndRoundUri);

        // combine the driver and constructor as that is what we are expecting back
        ContentValues resultWithDriverAndConstructorValues = new ContentValues();
        resultWithDriverAndConstructorValues.putAll(resultValues);
        resultWithDriverAndConstructorValues.putAll(driverValues);
        resultWithDriverAndConstructorValues.putAll(constructorValues);

        validateCursor(resultWithDriverAndConstructorValues, resultCursor);
        resultCursor.close();

        // result/2008/5/1
        Uri resultWithAndRoundWithPositionUri = ResultsEntry
                .buildResultsWithSeasonAndRoundAndPositionUri(season, round, position);
        resultCursor = simpleQuery(resultWithAndRoundWithPositionUri);

        // we can use the already constructed content values
        validateCursor(resultWithDriverAndConstructorValues, resultCursor);
        resultCursor.close();
    }

    public void testDeleteAllRecords() {
        simpleDelete(DriverEntry.CONTENT_URI);
        simpleDelete(ConstructorEntry.CONTENT_URI);
        simpleDelete(CircuitEntry.CONTENT_URI);
        simpleDelete(RaceEntry.CONTENT_URI);
        simpleDelete(ResultsEntry.CONTENT_URI);

        Cursor driverCursor = simpleQuery(DriverEntry.CONTENT_URI);
        Cursor constructorCursor = simpleQuery(ConstructorEntry.CONTENT_URI);
        Cursor circuitCursor = simpleQuery(CircuitEntry.CONTENT_URI);
        Cursor raceCursor = simpleQuery(RaceEntry.CONTENT_URI);
        Cursor resultCursor = simpleQuery(ResultsEntry.CONTENT_URI);

        assertEquals(driverCursor.getCount(), 0);
        assertEquals(constructorCursor.getCount(), 0);
        assertEquals(circuitCursor.getCount(), 0);
        assertEquals(raceCursor.getCount(), 0);
        assertEquals(resultCursor.getCount(), 0);
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

        // RACE
        // content://com.allendolph.f1results/race
        type = mContext.getContentResolver().getType(RaceEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.allendolph.f1results/race
        assertEquals(RaceEntry.CONTENT_TYPE, type);

        String testSeason = "2008";
        // content://com.allendolph.f1results/race/2008
        type = mContext.getContentResolver().getType(RaceEntry.buildRaceSeasonUri(testSeason));
        // vnd.android.cursor.dir/com.allendolph.f1results/race/2008
        assertEquals(RaceEntry.CONTENT_TYPE, type);

        String testRound = "5";
        // content://com.allendolph.f1results/race/2008/5
        type = mContext.getContentResolver()
                .getType(RaceEntry.buildRaceSeasonWithRound(testSeason, testRound));
        // vnd.android.cursor.dir/com.allendolph.f1results/race/2008/5
        assertEquals(RaceEntry.CONTENT_ITEM_TYPE, type);

        // RESULT
        // content://com.allendolph.f1results/result
        type = mContext.getContentResolver().getType(ResultsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.allendolph.f1results/result
        assertEquals(ResultsEntry.CONTENT_TYPE, type);

        // content://com.allendolph.f1results/result/2008/5
        type = mContext.getContentResolver()
                .getType(ResultsEntry.buildResultWithSeasonAndRoundUri(testSeason, testRound));
        // vnd.android.cursor.dir/com.allendolph.f1results/result/2008/5
        assertEquals(ResultsEntry.CONTENT_TYPE, type);

        String testPosition = "1";
        // content://com.allendolph.f1results/result/2008/5/1
        type = mContext.getContentResolver()
                .getType(ResultsEntry
                .buildResultsWithSeasonAndRoundAndPositionUri(testSeason, testRound, testPosition));
        // vnd.android.cursor.dir/com.allendolph.f1results/result/2008/5/1
        assertEquals(ResultsEntry.CONTENT_ITEM_TYPE, type);
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
