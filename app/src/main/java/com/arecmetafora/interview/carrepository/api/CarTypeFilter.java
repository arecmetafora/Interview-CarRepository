package com.arecmetafora.interview.carrepository.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arecmetafora.interview.carrepository.R;

import retrofit2.Call;

/**
 * Filter to select a car type.
 */
public class CarTypeFilter extends CarCharacteristicFilter {

    /**
     * Car Manufacturer filter, which this filter relies on.
     */
    public ManufacturerFilter mManufacturerFilter;

    /**
     * Creates a filter for car manufactures.
     *
     * @param context The application context.
     */
    public CarTypeFilter(@NonNull Context context, @NonNull ManufacturerFilter manufacturerFilter) {
        super(context.getString(R.string.filter_select_type));
        this.mManufacturerFilter = manufacturerFilter;
    }

    @Override
    public boolean preRequisitesWereFulfilled() {
        return mManufacturerFilter.getSelectedCharacteristic() != null;
    }

    @Override
    protected Call<ApiResponse> loadCharacteristics(@NonNull CarRepositoryApi api, int page) {
        return api.getCarTypes(mManufacturerFilter.getSelectedCharacteristic().id, page);
    }
}
