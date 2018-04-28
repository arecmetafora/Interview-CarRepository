package com.arecmetafora.interview.carrepository.api;

import android.app.Application;
import android.support.annotation.NonNull;

import retrofit2.Call;

public class ManufacturerFilter extends CarCharacteristicFilter<ManufacturerFilter.ViewModel> {

    public static class ViewModel extends CarCharacteristicsViewModel {

        public ViewModel(@NonNull Application application) {
            super(application);
        }

        @Override
        protected Call<ApiResponse> loadCharacteristics(CarRepositoryApi api, int page) {
            return api.getManufactures(page);
        }
    }
}
