package com.allendolph.f1results;

import android.content.ContentValues;

import com.allendolph.f1results.data.F1Contract;

/**
 * Created by allendolph on 4/1/15.
 */
public class TestUtil {

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
}
