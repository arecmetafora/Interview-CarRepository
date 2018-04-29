package com.arecmetafora.interview.carrepository.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * REST api for the car repository service.
 */
public interface CarRepositoryApi {

    @GET("manufacturer")
    Call<ApiResponse> getManufactures(
            @Query("page") int page);

    @GET("main-types")
    Call<ApiResponse> getCarTypes(
            @Query("manufacturer") String manufacturer,
            @Query("page") int page);

    @GET("built-dates")
    Call<ApiResponse> getBuiltDates(
            @Query("manufacturer") String manufacturer,
            @Query("main-type") String carType,
            @Query("page") int page);
}
