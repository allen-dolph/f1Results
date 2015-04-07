package com.allendolph.f1results.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.allendolph.f1results.data.F1Contract.DriverEntry;
import com.allendolph.f1results.data.F1Contract.ConstructorEntry;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;
import com.allendolph.f1results.data.F1Contract.ResultsEntry;

/**
 * Created by allendolph on 3/30/15.
 */
public class F1Provider extends ContentProvider {
    private static final int DRIVER = 100;
    private static final int DRIVER_ID = 101;
    private static final int CONSTRUCTOR = 200;
    private static final int CONSTRUCTOR_ID = 201;
    private static final int CIRCUIT = 300;
    private static final int CIRCUIT_ID = 301;
    private static final int RACE = 400;
    private static final int RACE_SEASON = 401;
    private static final int RACE_SEASON_ROUND_WITH_CIRCUIT = 402;
    private static final int RESULT = 500;
    private static final int RESULT_SEASON_AND_ROUND = 501;
    private static final int RESULT_SEASON_AND_ROUND_AND_POSITION = 502;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private F1DbHelper mOpenHelper;

    // Helper query builders for composite result sets
    private static final SQLiteQueryBuilder sRaceBySeasonAndRoundQueryBuilder;
    private static final SQLiteQueryBuilder sResultQueryBuilder;

    static {
        sRaceBySeasonAndRoundQueryBuilder = new SQLiteQueryBuilder();
        sRaceBySeasonAndRoundQueryBuilder.setTables(
                RaceEntry.TABLE_NAME + " INNER JOIN " +
                        CircuitEntry.TABLE_NAME +
                        " ON " + RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_CIRCUIT_ID +
                        " = " + CircuitEntry.TABLE_NAME + "." + CircuitEntry._ID);
    }

    static {
        sResultQueryBuilder = new SQLiteQueryBuilder();
        sResultQueryBuilder.setTables(
                ResultsEntry.TABLE_NAME + " INNER JOIN " +
                        DriverEntry.TABLE_NAME +
                        " ON " + ResultsEntry.TABLE_NAME + "." + ResultsEntry.COLUMN_DRIVER_ID +
                        " = " + DriverEntry.TABLE_NAME + "." + DriverEntry._ID +
                        " INNER JOIN " + ConstructorEntry.TABLE_NAME +
                        " ON " + ResultsEntry.TABLE_NAME + "." + ResultsEntry.COLUMN_CONSTRUCTOR_ID +
                        " = " + ConstructorEntry.TABLE_NAME + "." + ConstructorEntry._ID +
                        " LEFT JOIN " + RaceEntry.TABLE_NAME +
                        " ON " + ResultsEntry.TABLE_NAME + "." + ResultsEntry.COLUMN_RACE_ID +
                        " = " + RaceEntry.TABLE_NAME + "." + RaceEntry._ID);
    }

    private static final String sRaceSeasonWithRoundSelection =
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_SEASON + " = ? AND " +
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_ROUND + " = ?";

    private static final String sResultSeasonWithRoundSelection =
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_SEASON + " = ? AND " +
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_ROUND + " = ?";

    private static final String sResultSeasonWithRoundWithPositionSelection =
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_SEASON + " = ? AND " +
            RaceEntry.TABLE_NAME + "." + RaceEntry.COLUMN_ROUND + " = ? AND " +
            ResultsEntry.TABLE_NAME + "." + ResultsEntry.COLUMN_POSITION + " = ?";

    private Cursor getRaceBySeasonAndRoundWithCircuit(Uri uri, String[] projection, String sortOrder) {
        String season = RaceEntry.getSeasonFromUri(uri);
        String round = RaceEntry.getRoundFromUri(uri);
        return sRaceBySeasonAndRoundQueryBuilder.query(mOpenHelper.getReadableDatabase(),
            projection,
            sRaceSeasonWithRoundSelection,
            new String[] { season, round},
            null,
            null,
            sortOrder
        );
    }

    private Cursor getResultWithDriverAndConstructor(Uri uri, String[] projection, String sortOrder) {
        return sResultQueryBuilder.query(mOpenHelper.getReadableDatabase(),
            projection,
            null,
            null,
            null,
            null,
            sortOrder

        );
    }

    private Cursor getResultBySeasonAndRoundWithDriverAndConstructor(
            Uri uri, String[] projection, String sortOrder) {
        String season = ResultsEntry.getSeasonFromUri(uri);
        String round = ResultsEntry.getRoundFromUri(uri);

        return sResultQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sResultSeasonWithRoundSelection,
                new String[] { season, round },
                null,
                null,
                sortOrder
        );
    }

    private Cursor getResultBySeasonAndRoundAndPositionWithDriverAndConstructor(
            Uri uri, String[] projection, String sortOrder) {
        String season = ResultsEntry.getSeasonFromUri(uri);
        String round = ResultsEntry.getRoundFromUri(uri);
        String position = ResultsEntry.getPositionFromUri(uri);

        return sResultQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sResultSeasonWithRoundWithPositionSelection,
                new String[] { season, round, position },
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return
        // when a match is found
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = F1Contract.CONTENT_AUTHORITY;

        // For each type or URI you want to add, create a corresponding code.
        // Drivers
        matcher.addURI(authority, F1Contract.PATH_DRIVER, DRIVER);
        matcher.addURI(authority, F1Contract.PATH_DRIVER + "/#", DRIVER_ID);

        // Constructors
        matcher.addURI(authority, F1Contract.PATH_CONSTRUCTOR, CONSTRUCTOR);
        matcher.addURI(authority, F1Contract.PATH_CONSTRUCTOR + "/#", CONSTRUCTOR_ID);

        // Circuits
        matcher.addURI(authority, F1Contract.PATH_CIRCUIT, CIRCUIT);
        matcher.addURI(authority, F1Contract.PATH_CIRCUIT + "/#", CIRCUIT_ID);

        // Races
        matcher.addURI(authority, F1Contract.PATH_RACE, RACE);
        matcher.addURI(authority, F1Contract.PATH_RACE + "/#", RACE_SEASON);
        matcher.addURI(authority, F1Contract.PATH_RACE + "/#/#", RACE_SEASON_ROUND_WITH_CIRCUIT);

        // Results
        matcher.addURI(authority, F1Contract.PATH_RESULT, RESULT);
        matcher.addURI(authority, F1Contract.PATH_RESULT + "/#/#", RESULT_SEASON_AND_ROUND);
        matcher.addURI(authority, F1Contract.PATH_RESULT + "/#/#/#", RESULT_SEASON_AND_ROUND_AND_POSITION);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new F1DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is
        // and query database accordingly
        Cursor retCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            // "driver"
            case DRIVER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DriverEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "driver/#"
            case DRIVER_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DriverEntry.TABLE_NAME,
                        projection,
                        DriverEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "constructor"
            case CONSTRUCTOR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ConstructorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "constructor/#"
            case CONSTRUCTOR_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ConstructorEntry.TABLE_NAME,
                        projection,
                        DriverEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "circuit"
            case CIRCUIT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CircuitEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "circuit/#"
            case CIRCUIT_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CircuitEntry.TABLE_NAME,
                        projection,
                        CircuitEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "race"
            case RACE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "race/2008"
            case RACE_SEASON: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RaceEntry.TABLE_NAME,
                        projection,
                        RaceEntry.COLUMN_SEASON + " = '" + RaceEntry.getSeasonFromUri(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "race/2008/5"
            case RACE_SEASON_ROUND_WITH_CIRCUIT: {
                retCursor = getRaceBySeasonAndRoundWithCircuit(uri, projection, sortOrder);
                break;
            }
            // "result"
            case RESULT: {
                retCursor = getResultWithDriverAndConstructor(uri, projection, sortOrder);
                break;
            }
            // "result/2008/5"
            case RESULT_SEASON_AND_ROUND: {
                retCursor = getResultBySeasonAndRoundWithDriverAndConstructor(
                        uri, projection, sortOrder);
                break;
            }
            // "result/2008/5/1
            case RESULT_SEASON_AND_ROUND_AND_POSITION: {
                retCursor = getResultBySeasonAndRoundAndPositionWithDriverAndConstructor(
                        uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DRIVER:
                return DriverEntry.CONTENT_TYPE;
            case DRIVER_ID:
                return DriverEntry.CONTENT_ITEM_TYPE;
            case CONSTRUCTOR:
                return ConstructorEntry.CONTENT_TYPE;
            case CONSTRUCTOR_ID:
                return ConstructorEntry.CONTENT_ITEM_TYPE;
            case CIRCUIT:
                return CircuitEntry.CONTENT_TYPE;
            case CIRCUIT_ID:
                return CircuitEntry.CONTENT_ITEM_TYPE;
            case RACE:
            case RACE_SEASON:
                return RaceEntry.CONTENT_TYPE;
            case RACE_SEASON_ROUND_WITH_CIRCUIT:
                return RaceEntry.CONTENT_ITEM_TYPE;
            case RESULT:
            case RESULT_SEASON_AND_ROUND:
                return ResultsEntry.CONTENT_TYPE;
            case RESULT_SEASON_AND_ROUND_AND_POSITION:
                return ResultsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case DRIVER: {
                long _id = db.insert(DriverEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = DriverEntry.buildDriverUri(_id);
                } else {
                    returnUri = null;
                }
                break;
            }
            case CONSTRUCTOR: {
                long _id = db.insert(ConstructorEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = ConstructorEntry.buildConstructorUri(_id);
                } else {
                    returnUri = null;
                }
                break;
            }
            case CIRCUIT: {
                long _id = db.insert(CircuitEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = CircuitEntry.buildCircuitUri(_id);
                } else {
                    returnUri = null;
                }
                break;
            }
            case RACE: {
                long _id = db.insert(RaceEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = RaceEntry.buildRaceSeasonWithRound(
                            values.getAsString(RaceEntry.COLUMN_SEASON),
                            values.getAsString(RaceEntry.COLUMN_ROUND)
                    );
                } else {
                    returnUri = null;
                }
                break;
            }
            case RESULT: {
                long _id = db.insert(ResultsEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    // we need to get the associated season and round from the result to
                    Cursor raceCursor = mOpenHelper.getReadableDatabase().query(
                            RaceEntry.TABLE_NAME,
                            new String[] {
                                    RaceEntry._ID,
                                    RaceEntry.COLUMN_SEASON,
                                    RaceEntry.COLUMN_ROUND
                            },
                            RaceEntry._ID + " = '" + values.getAsString(ResultsEntry.COLUMN_RACE_ID) + "'",
                            null,
                            null,
                            null,
                            null
                    );
                    if(!raceCursor.moveToFirst()) {
                        returnUri = null;
                    }

                    // get the season and round
                    String season = String.valueOf(
                            raceCursor.getLong(raceCursor.getColumnIndex(RaceEntry.COLUMN_SEASON)));
                    String round = String.valueOf(
                            raceCursor.getLong(raceCursor.getColumnIndex(RaceEntry.COLUMN_ROUND)));

                    returnUri = ResultsEntry.buildResultsWithSeasonAndRoundAndPositionUri(
                            season,
                            round,
                            values.getAsString(ResultsEntry.COLUMN_POSITION)
                    );
                } else {
                    returnUri = null;
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case DRIVER:
                rowsDeleted = db.delete(DriverEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONSTRUCTOR:
                rowsDeleted = db.delete(ConstructorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CIRCUIT:
                rowsDeleted = db.delete(CircuitEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RACE:
                rowsDeleted = db.delete(RaceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RESULT:
                rowsDeleted = db.delete(ResultsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
