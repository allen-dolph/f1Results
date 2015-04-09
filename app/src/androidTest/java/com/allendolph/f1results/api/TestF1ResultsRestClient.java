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
    static final int LIMIT = 1000;

    public void testGetSchedule() {

        Response response = F1ResultsRestClient.getInstance().getScheduleResponse(SEASON, LIMIT);
        assertNotNull(response);

        String body = F1ResultsRestClient.getResponseBody(response);
        assertNotNull(body);

        F1ResultResponse scheduleResponse =
                F1ResultsRestClient.getInstance().getScheduleSync(SEASON, LIMIT);
        assertNotNull(scheduleResponse);

        F1ResultResponse resultResponse =
                F1ResultsRestClient.getInstance().getResultsSync(SEASON, LIMIT);

        assertNotNull(resultResponse);
    }
}
