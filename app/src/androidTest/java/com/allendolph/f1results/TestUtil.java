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
}
