package com.arecmetafora.interview.carrepository.api;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
     * Description of this filter.
     */
    private String mFilterDescription;

    /**
     * Filters which depends on this filter.
     */
    private List<CarCharacteristicFilter> descendants = new LinkedList<>();

    /**
     * Creates a new car characteristic filter.
     * @param filterDescription Description of this filter.
     */
    protected CarCharacteristicFilter(String filterDescription) {
        mFilterDescription = filterDescription;
    }

    /**
     * Selects a given car characteristics for this filter.
     *
     * @param carCharacteristic The car characteristic to be selected.
     */
    public void setSelectedCharacteristic(CarCharacteristic carCharacteristic) {
        if((mSelectedCharacteristic == null && carCharacteristic != null) ||
           (mSelectedCharacteristic != null && carCharacteristic == null) ||
                (mSelectedCharacteristic != null &&
                        !mSelectedCharacteristic.id.equals(carCharacteristic.id))) {
            // Clear selection of descendants
            for(CarCharacteristicFilter descendantFilter : descendants) {
                descendantFilter.setSelectedCharacteristic(null);
            }
        }
        mSelectedCharacteristic = carCharacteristic;
    }

    /**
     * Add a filter as dependency of this filter.
     *
     * @param filter The dependent filter.
     */
    protected void addDependency(CarCharacteristicFilter filter) {
        descendants.add(filter);
    }


    /**
     * @return The selected value of this filter.
     */
    public CarCharacteristic getSelectedCharacteristic() {
        return mSelectedCharacteristic;
    }

    /**
     * @return The description of this filter.
     */
    public String getFilterDescription() {
        return mFilterDescription;
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

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) &&
                mFilterDescription.equals(((CarCharacteristicFilter)obj).mFilterDescription);
    }
}
