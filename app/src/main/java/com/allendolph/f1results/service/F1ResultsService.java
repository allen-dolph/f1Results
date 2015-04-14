package com.allendolph.f1results.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.allendolph.f1results.api.F1ResultsModel;
import com.allendolph.f1results.api.F1ResultsModel.F1ResultResponse;
import com.allendolph.f1results.api.F1ResultsRestClient;
import com.allendolph.f1results.data.F1Contract;
import com.allendolph.f1results.data.F1Contract.CircuitEntry;
import com.allendolph.f1results.data.F1Contract.RaceEntry;

import java.util.concurrent.Callable;

/**
 * Created by allendolph on 4/8/15.
 */
public class F1ResultsService extends IntentService {

    private static final String LOG_TAG = "F1RESULTS_SERVICE";
    private static final int LIMIT = 1000;

    // Intent Extra tags
    public static final String SEASON_EXTRA = "seasonExtra";

    public F1ResultsService() {
        super("F1ResultsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // make sure the intent has all the required extras
        if(!intent.hasExtra(SEASON_EXTRA)) {
            return;
        }

        String season = intent.getStringExtra(SEASON_EXTRA);


        // We're going to use the "big cookie" model here so will use our retrofit interface
        // to the api to get all data required for the application for the selected season
        // this includes both schedule and full results set
        //
        // for now we'll use the syncronous methods because this is happening in the background
        // service.  In the future we can update to use the async method so we can make both calls
        // at the same time.
        F1ResultResponse scheduleResponse = F1ResultsRestClient.getInstance()
                .getScheduleSync(season, LIMIT, null);
        F1ResultResponse resultResponse = F1ResultsRestClient.getInstance()
                .getResultsSync(season, LIMIT, null);

        // now that we have the necessary data, we need to parse and add to the database
        // TODO

        // first insert the schedule response data
        addUpdateScheduleData(scheduleResponse);
    }

    private void addUpdateScheduleData(F1ResultResponse scheduleResponse) {
        //verify that the the response has races to post to the schedule
        String season;
        //validate the response
        if(scheduleResponse.mrData == null ||
                scheduleResponse.mrData.raceTable == null ||
                scheduleResponse.mrData.raceTable.season == "" ||
                scheduleResponse.mrData.raceTable.races == null) {
            return;
        }
        season = scheduleResponse.mrData.raceTable.season;

        // check if the season is already in the database
        for(F1ResultsModel.Race race : scheduleResponse.mrData.raceTable.races) {
            // first we need to check if the circuit is already in the database
            Cursor circuitCursor = getContentResolver().query(
                    CircuitEntry.CONTENT_URI,
                    new String[] { CircuitEntry._ID },
                    CircuitEntry.COLUMN_CIRCUIT_ID + " = '" + race.circuit.circuitId + "'",
                    null,
                    null,
                    null
            );

            long circuitId;
            if(circuitCursor.moveToFirst()) {
                circuitId = circuitCursor
                       .getLong(circuitCursor.getColumnIndex(CircuitEntry._ID));
            } else {
                ContentValues circuitValues = race.circuit.getAsCircuitEntryContentValues();
                Uri circuitUri = getContentResolver()
                        .insert(CircuitEntry.CONTENT_URI, circuitValues);
                circuitId = Long.parseLong(CircuitEntry.getCircuitFromUri(circuitUri));
            }

            Uri raceUri = RaceEntry
                    .buildRaceSeasonWithRound(race.season, String.valueOf(race.round));
            Cursor raceCursor = simpleQuery(raceUri);


            // if the there is already a race we need to update, else insert
            ContentValues raceValues = race.getAsRaceEntryContentValues(circuitId);
            if(raceCursor.moveToFirst()) {
                Uri raceWithSeasonAndRoundUri = RaceEntry.buildRaceSeasonWithRound(
                        race.season,
                        String.valueOf(race.round)
                );
                getContentResolver().update(raceWithSeasonAndRoundUri, raceValues, null, null);
            } else {
                raceUri = getContentResolver().insert(RaceEntry.CONTENT_URI, raceValues);
            }
        }
    }

    // helper methods so that the code reads cleaner
    private Cursor simpleQuery(Uri uri) {
        return getContentResolver().query(uri, null, null, null, null);
    }

    static public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, F1ResultsService.class);
            sendIntent.putExtra(SEASON_EXTRA, intent.getStringExtra(SEASON_EXTRA));
            context.startService(sendIntent);
        }
    }
}
