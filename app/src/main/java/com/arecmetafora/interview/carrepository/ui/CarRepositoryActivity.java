package com.arecmetafora.interview.carrepository.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class CarRepositoryActivity extends DaggerAppCompatActivity
        implements CarCharacteristicChooser.CarCharacteristicChooserListener,
                   CarCharacteristicFilterListAdapter.CarCharacteristicFilterListener {

    @Inject
    List<CarCharacteristicFilter> mCarCharacteristicFilters;

    @Inject
    CarCharacteristicFilterListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_repository);

        RecyclerView list = findViewById(R.id.car_characteristic_filter_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);
        mAdapter.setListener(this);
    }

    @Override
    public void onCharacteristicSelected(CarCharacteristic characteristic) {

    }

    @Override
    public void onCharacteristicFilterOpened(CarCharacteristicFilter characteristicFilter) {
        CarCharacteristicChooser.newInstance(characteristicFilter)
                .show(getSupportFragmentManager(), "DIALOG");
    }
}
