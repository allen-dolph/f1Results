package com.allendolph.f1results.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by allendolph on 3/30/15.
 * Defines table and column names for the f1 database
 */
public class F1Contract {

    // Content Provider Name
    public static final String CONTENT_AUTHORITY = "com.allendolph.f1results";

    // Use CONTENT_AUTHORITY to create a uri which apps will use to contact
    // the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_RACE = "race";
    public static final String PATH_CIRCUIT = "circuit";
    public static final String PATH_RESULT = "result";
    public static final String PATH_DRIVER = "driver";
    public static final String PATH_CONSTRUCTOR = "constructor";
    public static final String PATH_STANDINGS = "standings";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /*
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT
     */
    public static String getDbDateString(java.util.Date date) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to a valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class RaceEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RACE).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RACE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RACE;

        // Table Name
        public static final String TABLE_NAME = "races";

        // Columns
        public static final String COLUMN_SEASON = "season";
        public static final String COLUMN_ROUND = "round";
        public static final String COLUMN_URL = "raceUrl";
        public static final String COLUMN_RACE_NAME = "raceName";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";

        // Foreign Key Columns
        public static final String COLUMN_CIRCUIT_ID = "circuit_id";

        // Content Uri Helper Methods
        public static Uri buildRaceSeasonUri(String season) {
            return CONTENT_URI.buildUpon().appendPath(season).build();
        }

        public static Uri buildRaceSeasonWithRound(String season, String round) {
            return CONTENT_URI.buildUpon().appendPath(season)
                    .appendPath(round).build();
        }

        public static String getSeasonFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getRoundFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class CircuitEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CIRCUIT).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CIRCUIT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CIRCUIT;

        // Table Name
        public static final String TABLE_NAME = "circuits";

        // Columns
        public static final String COLUMN_CIRCUIT_ID = "circuitId";
        public static final String COLUMN_URL = "circuitUrl";
        public static final String COLUMN_CIRCUIT_NAME = "circuitName";
        public static final String COLUMN_LOCATION_LAT = "lat";
        public static final String COLUMN_LOCATION_LONG = "long";
        public static final String COLUMN_LOCATION_LOCALITY = "locality";
        public static final String COLUMN_LOCATION_COUNTRY = "country";

        // Content Uri Helper Methods
        public static Uri buildCircuitUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getCircuitFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class DriverEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRIVER).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DRIVER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DRIVER;

        // Table Name
        public static final String TABLE_NAME = "drivers";

        // Columns
        public static final String COLUMN_DRIVER_ID = "driverId";
        public static final String COLUMN_PERMANENT_NUMBER = "permanentNumber";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_URL = "driverUrl";
        public static final String COLUMN_GIVEN_NAME = "givenName";
        public static final String COLUMN_FAMILY_NAME = "familyName";
        public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
        public static final String COLUMN_NATIONALITY = "nationality";

        // Content Uri Helper Methods
        public static Uri buildDriverUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getDriverFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class ConstructorEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONSTRUCTOR).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CONSTRUCTOR;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CONSTRUCTOR;

        // Table Name
        public static final String TABLE_NAME = "constructors";

        // Columns
        public static final String COLUMN_CONSTRUCTOR_ID = "constructorId";
        public static final String COLUMN_URL = "constructorUrl";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NATIONALITY = "nationality";

        // Content Uri Helper Methods
        public static Uri buildConstructorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getConstructorFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class ResultsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESULT).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RESULT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_RESULT;

        // Table Name
        public static final String TABLE_NAME = "results";

        // Columns
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_POSITION_TEXT = "positionText";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_GRID = "grid";
        public static final String COLUMN_LAPS = "laps";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TIME_MILLIS = "timeMillis";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_FASTEST_LAP_RANK = "fastestLapRank";
        public static final String COLUMN_FASTEST_LAP_LAP = "fastestLapLap";
        public static final String COLUMN_FASTEST_LAP_TIME = "fastedLapTime";
        public static final String COLUMN_FASTEST_LAP_AVG_SPEED_UNITS = "fastestLapAvgSpeedUnits";
        public static final String COLUMN_FASTEST_LAP_AVG_SPEED_SPEED = "fastestLapAvgSpeedSpeed";

        // Foreign Key Columns
        public static final String COLUMN_RACE_ID = "race_id";
        public static final String COLUMN_DRIVER_ID = "driver_id";
        public static final String COLUMN_CONSTRUCTOR_ID = "constructor_id";

        // Content Uri Helper Methods
        public static Uri buildResultUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getResultFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the results */
    public static final class StandingsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STANDINGS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STANDINGS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STANDINGS;

        // Table Name
        public static final String TABLE_NAME = "standings";

        // Columns
        public static final String COLUMN_SEASON = "season";
        public static final String COLUMN_ROUND = "round";
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_POSITION_TEXT = "positionText";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_WINS = "wins";

        // Foreign Key Columns
        public static final String COLUMN_DRIVER_ID = "driver_id";
        public static final String COLUMN_CONSTRUCTOR_ID = "constructor_id";

        // Content Uri Helper Methods
        public static Uri buildStandingsSeasonUri(String season) {
            return CONTENT_URI.buildUpon().appendPath(season).build();
        }

        public static Uri buildStandingsSeasonWithRound(String season, String round) {
            return CONTENT_URI.buildUpon().appendPath(season)
                    .appendPath(round).build();
        }

        public static String getSeasonFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getRoundFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }
}
