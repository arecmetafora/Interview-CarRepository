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

public abstract class CarCharacteristicsViewModel extends AndroidViewModel {

    @Inject
    protected CarRepositoryApi api;

    private MutableLiveData<List<CarCharacteristic>> mCharacteristics;

    CarCharacteristicsViewModel(@NonNull Application application) {
        super(application);
        ((CustomApplication)application).getAppComponent().inject(this);
    }

    public MutableLiveData<List<CarCharacteristic>> getCharacteristics() {
        if (mCharacteristics == null) {
            mCharacteristics = new MutableLiveData<>();
            loadCharacteristics();
        }
        return mCharacteristics;
    }

    protected abstract Call<ApiResponse> loadCharacteristics(CarRepositoryApi api, int page);

    private void loadCharacteristics() {
        loadCharacteristics(api, 0).enqueue(new Callback<ApiResponse>() {
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
