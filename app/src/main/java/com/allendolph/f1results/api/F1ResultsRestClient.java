package com.allendolph.f1results.api;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by allendolph on 4/8/15.
 */
public class F1ResultsRestClient {
    // singleton instance
    private static F1ResultsAPI REST_CLIENT;
    private static final String ROOT = "http://ergast.com/api/f1/";

    static {
        setupRestClient();
    }

    // private constructor for the singleton instance
    private F1ResultsRestClient() {}

    public static F1ResultsAPI getInstance() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(F1ResultsAPI.class);
    }

    // helper methods
    public static String getResponseBody(Response response) {
        //Try to get response body
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
}
