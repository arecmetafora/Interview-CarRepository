package com.arecmetafora.interview.carrepository.di;

import android.app.Application;
import android.content.Context;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarBuiltDateFilter;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;
import com.arecmetafora.interview.carrepository.api.CarRepositoryApi;
import com.arecmetafora.interview.carrepository.api.CarTypeFilter;
import com.arecmetafora.interview.carrepository.api.ManufacturerFilter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    /**
     * Size of the page used for each api request.
     */
    private static final int PAGE_SIZE = 15;

    @Binds
    abstract Context bindContext(Application application);

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Context context) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            HttpUrl newUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("pageSize", String.valueOf(PAGE_SIZE))
                    .addQueryParameter("wa_key", context.getString(R.string.api_token))
                    .build();

            Request newRequest = originalRequest.newBuilder().url(newUrl).build();

            return chain.proceed(newRequest);
        }).build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(Context context, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(context.getString(R.string.base_url))
                .build();
    }

    @Provides
    @Singleton
    static CarRepositoryApi provideCarRepositoryApi(Retrofit retrofit) {
        return retrofit.create(CarRepositoryApi.class);
    }

    @Provides
    @Singleton
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
