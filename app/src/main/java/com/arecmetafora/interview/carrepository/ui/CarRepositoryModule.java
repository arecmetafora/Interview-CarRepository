package com.arecmetafora.interview.carrepository.ui;

import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;
import com.arecmetafora.interview.carrepository.api.ManufacturerFilter;
import com.arecmetafora.interview.carrepository.di.ActivityScoped;
import com.arecmetafora.interview.carrepository.di.FragmentScoped;

import java.util.LinkedList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CarRepositoryModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CarCharacteristicChooser carCharacteristicChooser();

    @ActivityScoped
    @Provides
    static List<CarCharacteristicFilter> provideCarCharacteristicFilters() {
        List<CarCharacteristicFilter> filters = new LinkedList<>();
        filters.add(new ManufacturerFilter());
        filters.add(new ManufacturerFilter());
        filters.add(new ManufacturerFilter());
        return filters;
    }
}
