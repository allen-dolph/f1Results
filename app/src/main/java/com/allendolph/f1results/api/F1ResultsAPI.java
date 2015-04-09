package com.allendolph.f1results.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.allendolph.f1results.api.F1ResultsModel.F1ResultResponse;

/**
 * Created by allendolph on 4/8/15.
 */
public interface F1ResultsAPI {

    @GET("/{season}.json?limit={limit}")
    void getScheduleAsync(
            @Path("season") String season,
            @Query("limit") int limit,
            Callback<F1ResultResponse> callback);

    @GET("/{season}.json")
    F1ResultsModel.F1ResultResponse getScheduleSync(
            @Path("season") String season,
            @Query("limit") int limit);

    @GET("/{season}.json")
    Response getScheduleResponse(
            @Path("season") String season,
            @Query("limit") int limit);

    @GET("/{season}/results.json")
    void getResultsAsync(
            @Path("season") String season,
            @Query("limit") int limit,
            Callback<F1ResultResponse> callback);

    @GET("/{season}/results.json")
    F1ResultResponse getResultsSync(
            @Path("season") String season,
            @Query("limit") int limit);

    @GET("/{season}/results.json")
    Response getResultResponse(
            @Path("season") String season,
            @Query("limit") int limit);

}
