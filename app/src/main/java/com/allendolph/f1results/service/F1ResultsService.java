package com.allendolph.f1results.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by allendolph on 4/8/15.
 */
public class F1ResultsService extends IntentService {

    private static final String LOG_TAG = "F1RESULTS_SERVICE";

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

        // These need to be declared outside the try/catch so that
        // they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // will contain the raw JSON response as a string
        String resultJsonStr = null;

        String format = ".json";

        try {
            // Construct the URL for the Ergast F1 results API
            final String BASE_URL = "http://ergast.com/api/f1/";

            // we need to make multiple queries against the api

            // first for the race schedule
            // http://ergast.com/api/f1/2015
            Uri raceUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(season).build();

            URL raceUrl = new URL(raceUri.toString());

            // create the request to Ergast f1 api, and open the connection
            urlConnection = (HttpURLConnection) raceUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null) {
                // nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            // we we don't get the expected data back there is no reason to parse
            return;
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream: ", e);
                }
            }
        }
    }

}
