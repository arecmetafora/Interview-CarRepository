package com.arecmetafora.interview.carrepository.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.arecmetafora.interview.carrepository.api.ApiResponse;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarRepositoryApi;
import com.arecmetafora.interview.carrepository.di.CustomApplication;
import com.arecmetafora.interview.carrepository.di.DaggerAppComponent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarCharacteristicsViewModel extends AndroidViewModel {

    @Inject
    CarRepositoryApi api;

    private MutableLiveData<List<CarCharacteristic>> mData;

    public CarCharacteristicsViewModel(@NonNull Application application) {
        super(application);
        ((CustomApplication)application).getAppComponent().inject(this);
    }

    public MutableLiveData<List<CarCharacteristic>> getCharacteristics() {
        if (mData == null) {
            mData = new MutableLiveData<List<CarCharacteristic>>();
            loadCharacteristics();
        }
        return mData;
    }

    private void loadCharacteristics() {
        api.getManufactures(0).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                List<CarCharacteristic> list = new LinkedList<>();

                for(Map.Entry<String, String> values : response.body().wkda.entrySet()) {
                    CarCharacteristic c = new CarCharacteristic();
                    c.id = values.getKey();
                    c.name = values.getValue();
                    list.add(c);
                }

                mData.setValue(list);
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
