package com.allendolph.f1results.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.allendolph.f1results.data.F1Contract.DriverEntry;
import com.allendolph.f1results.data.F1Contract.ConstructorEntry;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;
import com.allendolph.f1results.data.F1Contract.ResultsEntry;

import java.sql.SQLException;

/**
 * Created by allendolph on 3/30/15.
 */
public class F1Provider extends ContentProvider {
    private static final int DRIVER = 100;
    private static final int DRIVER_ID = 101;
    private static final int CONSTRUCTOR = 200;
    private static final int CIRCUIT = 300;
    private static final int RACE = 400;
    private static final int RACE_SEASON = 401;
    private static final int RACE_SEASON_ROUND = 402;
    private static final int RESULT = 500;
    private static final int RESULT_RACE = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private F1DbHelper mOpenHelper;


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

        // Circuits
        matcher.addURI(authority, F1Contract.PATH_CIRCUIT, CIRCUIT);

        // Races
        matcher.addURI(authority, F1Contract.PATH_RACE, RACE);
        matcher.addURI(authority, F1Contract.PATH_RACE + "/#", RACE_SEASON);
        matcher.addURI(authority, F1Contract.PATH_RACE + "/#/#", RACE_SEASON_ROUND);

        // Results
        matcher.addURI(authority, F1Contract.PATH_RESULT, RESULT);
        matcher.addURI(authority, F1Contract.PATH_RESULT + "/#", RESULT_RACE);

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
            case CONSTRUCTOR:
                return ConstructorEntry.CONTENT_TYPE;
            case CIRCUIT:
                return CircuitEntry.CONTENT_TYPE;
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
                    //throw new SQLException("Failed to insert driver row into: " + uri);
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
