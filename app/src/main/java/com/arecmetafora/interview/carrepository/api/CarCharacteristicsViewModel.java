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
     * Whether data is still loading.
     */
    private boolean mIsLoading = false;

    /**
     * Last page that was loaded.
     */
    private int mLastLoadedPage;

    /**
     * Total number of pages which contains all the car characteristics
     */
    private int mTotalNumberOfPages;

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
            mCharacteristics.setValue(new LinkedList<>());
            loadCharacteristics(filter, 0);
        }

        return mCharacteristics;
    }

    /**
     * Loads the car characteristics for a given filter from REST service, notifying the listeners when it is done.
     *
     * @param filter The car characteristics filter.
     * @param page Page number to load.
     */
    private void loadCharacteristics(@NonNull CarCharacteristicFilter filter, int page) {
        mIsLoading = true;
        filter.loadCharacteristics(api, page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                List<CarCharacteristic> list = mCharacteristics.getValue();
                assert list != null;

                ApiResponse result = response.body();
                assert result != null;

                mTotalNumberOfPages = result.totalPageCount;
                mLastLoadedPage = result.page;

                for(Map.Entry<String, String> values : result.wkda.entrySet()) {
                    CarCharacteristic c = new CarCharacteristic();
                    c.id = values.getKey();
                    c.name = values.getValue();
                    list.add(c);
                }

                mCharacteristics.setValue(list);
                mIsLoading = false;
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                loadCharacteristics(filter, page);
            }
        });
    }

    /**
     * Load more car characteristics if there is another page to get.
     */
    public void loadMoreCharacteristics(@NonNull CarCharacteristicFilter filter) {
        if(mCharacteristics == null || mIsLoading) {
            return;
        }

        if(mLastLoadedPage + 1 < mTotalNumberOfPages) {
            loadCharacteristics(filter, mLastLoadedPage + 1);
        } else {
            mCharacteristics.setValue(null);
        }
    }
}
