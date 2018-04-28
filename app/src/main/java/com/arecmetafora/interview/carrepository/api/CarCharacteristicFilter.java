package com.arecmetafora.interview.carrepository.api;

import java.lang.reflect.ParameterizedType;

public abstract class CarCharacteristicFilter<T extends CarCharacteristicsViewModel> {

    private CarCharacteristic mSelectedCharacteristic;

    public CarCharacteristic getSelectedCharacteristic() {
        return mSelectedCharacteristic;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getViewModel() {
        ParameterizedType viewModelType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) (viewModelType).getActualTypeArguments()[0];
    }
}
