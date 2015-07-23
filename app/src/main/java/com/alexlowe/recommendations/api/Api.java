package com.alexlowe.recommendations.api;

import com.alexlowe.recommendations.model.ActiveListings;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Alex on 7/23/2015.
 */
public interface Api {

    @GET("/listings/active")
    void activeListings(@Query("includes") String includes,
                        retrofit.Callback<ActiveListings> callback);
}
