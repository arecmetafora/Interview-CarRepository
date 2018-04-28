package com.arecmetafora.interview.carrepository.ui;

import com.arecmetafora.interview.carrepository.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CarRepositoryModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CarCharacteristicChooser carCharacteristicChooser();
}
