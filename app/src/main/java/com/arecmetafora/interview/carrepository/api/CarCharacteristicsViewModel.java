package com.arecmetafora.interview.carrepository.api;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.arecmetafora.interview.carrepository.di.CustomApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Load manager for car characteristics, for a given filter.
 */
public class CarCharacteristicsViewModel extends AndroidViewModel {

    /**
     * REST api instance.
     */
    @Inject
    protected CarRepositoryApi api;

    /**
     * List of loaded characteristics.
     */
    private MutableLiveData<List<CarCharacteristic>> mCharacteristics;

    /**
     * Creates a new view model manager for this characteristic loader.
     *
     * @param application The current Android application.
     */
    CarCharacteristicsViewModel(@NonNull Application application) {
        super(application);
        ((CustomApplication)application).getAppComponent().inject(this);
    }

    /**
     * Gets the car characteristics for a given filter.
     *
     * @param filter The car characteristics filter.
     * @return List of loaded car characteristics for the given filter (may be loaded in the future. Observe it!)
     */
    public MutableLiveData<List<CarCharacteristic>> getCharacteristics(@NonNull CarCharacteristicFilter filter) {
        if (mCharacteristics == null) {
            mCharacteristics = new MutableLiveData<>();
            loadCharacteristics(filter);
        }
        return mCharacteristics;
    }

    /**
     * Loads the car characteristics for a given filter from REST service, notifying the listeners when it is done.
     *
     * @param filter The car characteristics filter.
     */
    private void loadCharacteristics(@NonNull CarCharacteristicFilter filter) {
        filter.loadCharacteristics(api, 0).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                List<CarCharacteristic> list = new LinkedList<>();

                assert response.body() != null;
                for(Map.Entry<String, String> values : response.body().wkda.entrySet()) {
                    CarCharacteristic c = new CarCharacteristic();
                    c.id = values.getKey();
                    c.name = values.getValue();
                    list.add(c);
                }

                mCharacteristics.setValue(list);
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
