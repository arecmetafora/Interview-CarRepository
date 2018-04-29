package com.arecmetafora.interview.carrepository.api;

import android.support.annotation.NonNull;

import java.io.Serializable;

import retrofit2.Call;

/**
 * Base class for car characteristic filters.
 */
public abstract class CarCharacteristicFilter implements Serializable {

    /**
     * Selected value of this filter.
     */
    private CarCharacteristic mSelectedCharacteristic;

    /**
     * Title of the chooser dialog for this filter.
     */
    private String mChooserTitle;

    /**
     * Creates a new car characteristic filter.
     * @param chooserTitle The title of the chooser dialog for this filter.
     */
    protected CarCharacteristicFilter(String chooserTitle) {
        mChooserTitle = chooserTitle;
    }

    /**
     * Selects a given car characteristics for this filter.
     *
     * @param carCharacteristic The car characteristic to be selected.
     */
    public void setSelectedCharacteristic(CarCharacteristic carCharacteristic) {
        mSelectedCharacteristic = carCharacteristic;
    }

    /**
     * @return The selected value of this filter.
     */
    public CarCharacteristic getSelectedCharacteristic() {
        return mSelectedCharacteristic;
    }

    /**
     * @return The title of the chooser dialog for this filter.
     */
    public String getChooserTitle() {
        return mChooserTitle;
    }

    /**
     * @return Whether all the pre-requisites of this filter were filled.
     */
    public abstract boolean preRequisitesWereFulfilled();

    /**
     * Loads more car characteristics for the current filter.
     *
     * @param api The api used to load the characteristics.
     * @param page The next page to load content.
     * @return The loaded characteristics for the current filter and for the given page.
     */
    protected abstract Call<ApiResponse> loadCharacteristics(@NonNull CarRepositoryApi api, int page);
}
