package com.arecmetafora.interview.carrepository.api;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.arecmetafora.interview.carrepository.R;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Filter to select a car manufacturer.
 */
public class ManufacturerFilter extends CarCharacteristicFilter {

    /**
     * Creates a filter for car manufactures.
     *
     * @param context The application context.
     */
    public ManufacturerFilter(@NonNull Context context) {
        super(context.getString(R.string.filter_select_manufacturer));
    }

    @Override
    public boolean preRequisitesWereFulfilled() {
        return true;
    }

    @Override
    protected Call<ApiResponse> loadCharacteristics(@NonNull CarRepositoryApi api, int page) {
        return api.getManufactures(page);
    }
}
