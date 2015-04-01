package com.allendolph.f1results.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.allendolph.f1results.data.F1Contract.DriverEntry;
import com.allendolph.f1results.data.F1Contract.ConstructorEntry;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;
import com.allendolph.f1results.data.F1Contract.ResultsEntry;

/**
 * Created by allendolph on 3/30/15.
 */
public class F1DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "f1.db";

    public F1DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Drivers Table
        final String SQL_CREATE_DRIVERS_TABLE =
                "CREATE TABLE " + DriverEntry.TABLE_NAME + "(" +
                        DriverEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DriverEntry.COLUMN_CODE + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_DRIVER_ID + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_FAMILY_NAME + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_GIVEN_NAME + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_NATIONALITY + " TEXT NOT NULL, " +
                        DriverEntry.COLUMN_PERMANENT_NUMBER + " INTEGER NOT NULL, " +
                        DriverEntry.COLUMN_URL + " TEXT NOT NULL, " +

                        // To assure the application only has one driver with a given
                        // driver id, crate a UNIQUE constraint with REPLACE strategy
                        " UNIQUE (" + DriverEntry.COLUMN_DRIVER_ID +
                        ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_DRIVERS_TABLE);

        // Create the Constructors Table
        final String SQL_CREATE_CONSTRUCTORS_TABLE =
                "CREATE TABLE " + ConstructorEntry.TABLE_NAME + "(" +
                        ConstructorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ConstructorEntry.COLUMN_CONSTRUCTOR_ID + " TEXT NOT NULL, " +
                        ConstructorEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        ConstructorEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        ConstructorEntry.COLUMN_NATIONALITY + " TEXT NOT NULL, " +

                        // To assure the application only has one constructor with a given
                        // constructor id, crate a UNIQUE constraint with REPLACE strategy
                        " UNIQUE (" + ConstructorEntry.COLUMN_CONSTRUCTOR_ID +
                        " ) ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CONSTRUCTORS_TABLE);

        // Create the Circuits Table
        final String SQL_CREATE_CIRCUITS_TABLE =
                "CREATE TABLE " + CircuitEntry.TABLE_NAME + "(" +
                        CircuitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CircuitEntry.COLUMN_CIRCUIT_ID + " TEXT NOT NULL, " +
                        CircuitEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        CircuitEntry.COLUMN_CIRCUIT_NAME + " TEXT NOT NULL," +
                        CircuitEntry.COLUMN_LOCATION_LAT + " REAL NOT NULL, " +
                        CircuitEntry.COLUMN_LOCATION_LONG + " REAL NOT NULL, " +
                        CircuitEntry.COLUMN_LOCATION_LOCALITY + " TEXT NOT NULL, " +
                        CircuitEntry.COLUMN_LOCATION_COUNTRY + " TEXT NOT NULL, " +

                        // To assure the application only has one circuit with a given
                        // circuit id, crate a UNIQUE constraint with REPLACE strategy
                        " UNIQUE (" + CircuitEntry.COLUMN_CIRCUIT_ID +
                        " ) ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CIRCUITS_TABLE);

        // Create the Race Table
        final String SQL_CREATE_RACE_TABLE =
                "CREATE TABLE " + RaceEntry.TABLE_NAME + "(" +
                        RaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RaceEntry.COLUMN_SEASON + " INTEGER NOT NULL, " +
                        RaceEntry.COLUMN_ROUND + " INTEGER NOT NULL, " +
                        RaceEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        RaceEntry.COLUMN_RACE_NAME + " TEXT NOT NULL, " +
                        RaceEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                        RaceEntry.COLUMN_TIME + " TEXT NOT NULL, " +

                        // foreign key columns
                        RaceEntry.COLUMN_CIRCUIT_ID + " INTEGER NOT NULL, " +

                        // Set up the circuit id column as a foreign key to location table
                        " FOREIGN KEY (" + RaceEntry.COLUMN_CIRCUIT_ID + ") REFERENCES " +
                        CircuitEntry.TABLE_NAME + " (" + CircuitEntry._ID + "));";

        db.execSQL(SQL_CREATE_RACE_TABLE);

        // Create the Results Table
        final String SQL_CREATE_RESULTS_TABLE =
                "CREATE TABLE " + ResultsEntry.TABLE_NAME + "(" +
                        ResultsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ResultsEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_POSITION + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_POSITION_TEXT + " TEXT NOT NULL, " +
                        ResultsEntry.COLUMN_POINTS + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_GRID + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_LAPS + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_STATUS + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_TIME_MILLIS + " TEXT NOT NULL, " +
                        ResultsEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                        ResultsEntry.COLUMN_FASTEST_LAP_RANK + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_FASTEST_LAP_LAP + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_FASTEST_LAP_TIME + " TEXT NOT NULL, " +
                        ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_UNITS + " TEXT NOT NULL, " +
                        ResultsEntry.COLUMN_FASTEST_LAP_AVG_SPEED_SPEED + " REAL NOT NULL, " +

                        // foreign key columns
                        ResultsEntry.COLUMN_DRIVER_ID + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_CONSTRUCTOR_ID + " INTEGER NOT NULL, " +
                        ResultsEntry.COLUMN_RACE_ID + " INTEGER NOT NULL, " +

                        // Setup the foreign key constraints for dirver, constructor and race
                        " FOREIGN KEY (" + ResultsEntry.COLUMN_DRIVER_ID + ") REFERENCES " +
                        DriverEntry.TABLE_NAME + " (" + DriverEntry._ID + "), " +
                        " FOREIGN KEY (" + ResultsEntry.COLUMN_CONSTRUCTOR_ID + ") REFERENCES " +
                        ConstructorEntry.TABLE_NAME + " (" + ConstructorEntry._ID + "), " +
                        " FOREIGN KEY (" + ResultsEntry.COLUMN_RACE_ID + ") REFERENCES " +
                        RaceEntry.TABLE_NAME + " (" + RaceEntry._ID + "));";
        db.execSQL(SQL_CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
