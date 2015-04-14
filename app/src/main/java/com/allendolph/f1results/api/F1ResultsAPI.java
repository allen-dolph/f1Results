package com.allendolph.f1results.api;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.allendolph.f1results.api.F1ResultsModel.F1ResultResponse;

import java.util.Objects;

/**
 * Created by allendolph on 4/8/15.
 */
public interface F1ResultsAPI {

    @GET("/{season}.json")
    void getScheduleAsync(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset,
            Callback<F1ResultResponse> callback);

    @GET("/{season}.json")
    F1ResultsModel.F1ResultResponse getScheduleSync(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset);

    @GET("/{season}.json")
    Response getScheduleResponse(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset);

    @GET("/{season}/results.json")
    void getResultsAsync(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset,
            Callback<F1ResultResponse> callback);

    @GET("/{season}/results.json")
    F1ResultResponse getResultsSync(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset);

    @GET("/{season}/results.json")
    Response getResultResponse(
            @Path("season") String season,
            @Query("limit") int limit,
            @Query("offset") Object offset);

    @GET("/{season}/{round}/results.json")
    void getRaceResultAsync(
            @Path("season") String season,
            @Path("round") String round,
            @Query("limit") int limit,
            @Query("offset") Object offset,
            Callback<F1ResultResponse> callback);

    @GET("/{season}/{round}/results.json")
    F1ResultResponse getRaceResultSync(
            @Path("season") String season,
            @Path("round") String round,
            @Query("limit") int limit,
            @Query("offset") Object offset);

    @GET("/{season}/{round}/results.json")
    Response getRaceResultResponse(
            @Path("season") String season,
            @Path("round") String round,
            @Query("limit") int limit,
            @Query("offset") Object offset);
}
