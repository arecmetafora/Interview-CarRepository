package com.arecmetafora.interview.carrepository.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarRepositoryApi {

    @GET("manufacturer")
    Call<ApiResponse> getManufactures(
            @Query("page") int page);

    @GET("main-types")
    Call<ApiResponse> getCarTypes(
            @Query("manufacturer") String manufacturer,
            @Query("page") int page);

    @GET("manufacturer")
    Call<ApiResponse> getManufactures(
            @Query("manufacturer") String manufacturer,
            @Query("main-type") String carType,
            @Query("page") int page);
}
