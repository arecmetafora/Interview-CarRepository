package com.arecmetafora.interview.carrepository.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arecmetafora.interview.carrepository.R;

import retrofit2.Call;

/**
 * Filter to select a car built date.
 */
public class CarBuiltDateFilter extends CarCharacteristicFilter {

    /**
     * Car Manufacturer filter, which this filter relies on.
     */
    private ManufacturerFilter mManufacturerFilter;

    /**
     * Car Type filter, which this filter relies on.
     */
    private CarTypeFilter mCarTypeFilter;

    /**
     * Creates a filter for car built date.
     *
     * @param context The application context.
     */
    public CarBuiltDateFilter(@NonNull Context context, @NonNull ManufacturerFilter manufacturerFilter,
                              @NonNull CarTypeFilter carTypeFilter) {
        super(context.getString(R.string.filter_select_built));
        this.mManufacturerFilter = manufacturerFilter;
        this.mManufacturerFilter.addDependency(this);
        this.mCarTypeFilter = carTypeFilter;
        this.mCarTypeFilter.addDependency(this);
    }

    @Override
    public boolean preRequisitesWereFulfilled() {
        return mManufacturerFilter.getSelectedCharacteristic() != null &&
                mCarTypeFilter.getSelectedCharacteristic() != null;
    }

    @Override
    protected Call<ApiResponse> loadCharacteristics(@NonNull CarRepositoryApi api, int page) {
        return api.getBuiltDates(
                mManufacturerFilter.getSelectedCharacteristic().id,
                mCarTypeFilter.getSelectedCharacteristic().id,
                page);
    }
}
