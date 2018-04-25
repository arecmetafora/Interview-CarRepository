package com.arecmetafora.interview.carrepository.di;

import com.arecmetafora.interview.carrepository.ui.CarRepositoryActivity;
import com.arecmetafora.interview.carrepository.ui.CarRepositoryModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = CarRepositoryModule.class)
    abstract CarRepositoryActivity carRepositoryActivity();
}
