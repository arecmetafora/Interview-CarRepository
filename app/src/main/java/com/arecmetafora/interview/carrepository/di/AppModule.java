package com.arecmetafora.interview.carrepository.di;

import android.app.Application;
import android.content.Context;

import com.arecmetafora.interview.carrepository.R;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a Dagger module. We use this to bind our CustomApplication class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our CustomApplication instance to any module,
 * we simply need to expose our CustomApplication as Context.
 * One of the advantages of Dagger.Android is that your
 * CustomApplication & Activities are provided into your graph for you.
 * {@link AppComponent}.
 */
@Module
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

    @Provides
    @Singleton
    static Retrofit providesRetrofit(Application application) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(application.getString(R.string.base_url))
                .build();
    }
}
