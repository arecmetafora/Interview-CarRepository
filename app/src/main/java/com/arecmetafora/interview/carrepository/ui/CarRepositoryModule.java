package com.arecmetafora.interview.carrepository.ui;

import android.content.Context;

import com.arecmetafora.interview.carrepository.api.CarBuiltDateFilter;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;
import com.arecmetafora.interview.carrepository.api.ManufacturerFilter;
import com.arecmetafora.interview.carrepository.api.CarTypeFilter;
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
    static List<CarCharacteristicFilter> provideCarCharacteristicFilters(Context context) {

        List<CarCharacteristicFilter> filters = new LinkedList<>();

        ManufacturerFilter manufacturerFilter = new ManufacturerFilter(context);
        filters.add(manufacturerFilter);

        CarTypeFilter carTypeFilter = new CarTypeFilter(context, manufacturerFilter);
        filters.add(carTypeFilter);

        CarBuiltDateFilter builtDateFilter = new CarBuiltDateFilter(context, manufacturerFilter, carTypeFilter);
        filters.add(builtDateFilter);

        return filters;
    }
}
