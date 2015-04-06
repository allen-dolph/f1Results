package com.allendolph.f1results;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.allendolph.f1results.data.F1Contract;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by allendolph on 4/1/15.
 */
public class TestUtil {

    public static final long RACE_SEASON = 2008;
    public static final long RACE_ROUND = 5;
    public static final long RESULT_POSITION = 1;


    public static Map<String, String> getValuesFromCursor(Cursor cursor, int index) {
        Map<String, String> map = new HashMap<>();

        if(!cursor.moveToFirst()) {
            return map;
        }

        if(!cursor.move(index)) {
            return map;
        }

        for(int i = 0; i < cursor.getColumnCount(); i++) {
            map.put(cursor.getColumnName(i), cursor.getString(i));
        }

        return map;
    }

    // Helper methods to get values to insert for the tests
    public static ContentValues getDriverContentValues() {
        ContentValues values = new ContentValues();

        String driverId = "hamilton";
        int permanentNumber = 44;
        String code = "HAM";
        String url = "http://testurl.com";
        String givenName = "Lewis";
        String familyName = "Hamilton";
        String dateOfBirth = "1985-01-07";
        String nationality = "British";

        values.put(F1Contract.DriverEntry.COLUMN_DRIVER_ID, driverId);
        values.put(F1Contract.DriverEntry.COLUMN_PERMANENT_NUMBER, permanentNumber);
        values.put(F1Contract.DriverEntry.COLUMN_CODE, code);
        values.put(F1Contract.DriverEntry.COLUMN_URL, url);
        values.put(F1Contract.DriverEntry.COLUMN_GIVEN_NAME, givenName);
        values.put(F1Contract.DriverEntry.COLUMN_FAMILY_NAME, familyName);
        values.put(F1Contract.DriverEntry.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        values.put(F1Contract.DriverEntry.COLUMN_NATIONALITY, nationality);

        return values;
    }

    public static ContentValues getConstructorContentValues() {
        ContentValues values = new ContentValues();

        String constructorId = "mclaren";
        String url = "http://test.com";
        String name = "McLaren";
        String nationality = "British";

        values.put(F1Contract.ConstructorEntry.COLUMN_CONSTRUCTOR_ID, constructorId);
        values.put(F1Contract.ConstructorEntry.COLUMN_URL, url);
        values.put(F1Contract.ConstructorEntry.COLUMN_NAME, name);
        values.put(F1Contract.ConstructorEntry.COLUMN_NATIONALITY, nationality);

        return values;
    }

    public static ContentValues getCircuitContentValues() {
        ContentValues values = new ContentValues();

        String circuitId = "albert_park";
        String url = "http://test.circuit.com";
        String circuitName = "Albert Park Grand Prix Circuit";
        double locationLat = -37.8497;
        double locationLong = 144.968;
        String locality = "Melbourne";
        String country = "Australia";

        values.put(F1Contract.CircuitEntry.COLUMN_CIRCUIT_ID, circuitId);
        values.put(F1Contract.CircuitEntry.COLUMN_URL, url);
        values.put(F1Contract.CircuitEntry.COLUMN_CIRCUIT_NAME, circuitName);
        values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LAT, locationLat);
        values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LONG, locationLong);
        values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LOCALITY, locality);
        values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_COUNTRY, country);

        return values;
    }

    public static ContentValues getRaceContentValues(long circuitId) {
        ContentValues values = new ContentValues();

        long season = RACE_SEASON;
        long round = RACE_ROUND;
        String url = "http://test.race.url";
        String raceName = "Australian Grand Prix";
        String date = "2008-03-16";
        String time = "04:30:00Z";

        values.put(F1Contract.RaceEntry.COLUMN_CIRCUIT_ID, circuitId);

        values.put(F1Contract.RaceEntry.COLUMN_SEASON, season);
        values.put(F1Contract.RaceEntry.COLUMN_ROUND, round);
        values.put(F1Contract.RaceEntry.COLUMN_URL, url);
        values.put(F1Contract.RaceEntry.COLUMN_RACE_NAME, raceName);
        values.put(F1Contract.RaceEntry.COLUMN_DATE, date);
        values.put(F1Contract.RaceEntry.COLUMN_TIME, time);

        return values;
    }

    public static ContentValues getResultContentValues(long driverId, long constructorId, long raceId) {
        ContentValues values = new ContentValues();

        long number = 22;
        long position = RESULT_POSITION;
        String positionText = "1";
        long points = 10;
        long grid = 1;
        long laps = 58;
        String status = "Finished";
        String millis = "5690616";
        String time = "1:34:50:616";
        long fastestLapRank = 2;
        long fastestLapLap = 39;
        String fastestLapTime = "1:27:452";
        String averageSpeedUnits = "kph";
        double averageSpeedSpeed = 218.300;

        values.put(F1Contract.ResultsEntry.COLUMN_DRIVER_ID, driverId);
        values.put(F1Contract.ResultsEntry.COLUMN_CONSTRUCTOR_ID, constructorId);
        values.put(F1Contract.ResultsEntry.COLUMN_RACE_ID, raceId);

        values.put(F1Contract.ResultsEntry.COLUMN_NUMBER, number);
        values.put(F1Contract.ResultsEntry.COLUMN_POSITION, position);
        values.put(F1Contract.ResultsEntry.COLUMN_POSITION_TEXT, positionText);
        values.put(F1Contract.ResultsEntry.COLUMN_POINTS, points);
        values.put(F1Contract.ResultsEntry.COLUMN_GRID, grid);
        values.put(F1Contract.ResultsEntry.COLUMN_LAPS, laps);
        values.put(F1Contract.ResultsEntry.COLUMN_STATUS, status);
        values.put(F1Contract.ResultsEntry.COLUMN_TIME_MILLIS, millis);
        values.put(F1Contract.ResultsEntry.COLUMN_TIME, time);
        values.put(F1Contract.ResultsEntry.COLUMN_FASTEST_LAP_RANK, fastestLapRank);
        values.put(F1Contract.ResultsEntry.COLUMN_FASTEST_LAP_LAP, fastestLapLap);
        values.put(F1Contract.ResultsEntry.COLUMN_FASTEST_LAP_TIME, fastestLapTime);
        values.put(F1Contract.ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_UNITS, averageSpeedUnits);
        values.put(F1Contract.ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_SPEED, averageSpeedSpeed);

        return values;
    }
}
