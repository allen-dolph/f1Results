package com.allendolph.f1results.api;

import android.test.AndroidTestCase;

import com.allendolph.f1results.api.F1ResultsModel.F1ResultResponse;

import retrofit.client.Response;

/**
 * Created by allendolph on 4/8/15.
 */
public class TestF1ResultsRestClient extends AndroidTestCase {
    static final String LOG_TAG = "TEST_REST_CLIENT";
    static final String SEASON = "2014";
    static final String ROUND = "5";
    static final int LIMIT = 1000;


    public void testGetSchedule() {

        // test getting a basic response
        Response response = F1ResultsRestClient
                .getInstance()
                .getScheduleResponse(SEASON, LIMIT, null);

        assertNotNull(response);

        // make sure we have a body
        String body = F1ResultsRestClient.getResponseBody(response);
        assertNotNull(body);

        // test serialization and to get an actual response
        F1ResultResponse scheduleResponse =
                F1ResultsRestClient.getInstance().getScheduleSync(SEASON, LIMIT, null);

        assertNotNull(scheduleResponse);
        assertTrue(scheduleResponse.mrData.total > 0);

    }

    public void testGetResults() {

        // test getting the race result list
        F1ResultResponse resultResponse =
                F1ResultsRestClient.getInstance().getResultsSync(SEASON, LIMIT, null);

        assertNotNull(resultResponse);
        assertTrue(resultResponse.mrData.total > 0);

        // test getting a individual race result
        F1ResultResponse raceResultResponse =
                F1ResultsRestClient.getInstance().getRaceResultSync(SEASON, ROUND, LIMIT, null);

        assertNotNull(raceResultResponse);
        assertNotNull(raceResultResponse.mrData.raceTable.races[0].results);
    }
}
