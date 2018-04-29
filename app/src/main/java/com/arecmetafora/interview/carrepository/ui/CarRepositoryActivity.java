package com.arecmetafora.interview.carrepository.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Main Activity of this project, to provide an interface to users to select a list of car based on
 * some filters.
 */
public class CarRepositoryActivity extends DaggerAppCompatActivity
        implements CarCharacteristicChooser.CarCharacteristicChooserListener,
                   CarCharacteristicFilterListAdapter.CarCharacteristicFilterListener {

    /**
     * List of car characteristic filters.
     */
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
    public void onCharacteristicFilterOpened(CarCharacteristicFilter characteristicFilter) {
        if(characteristicFilter.preRequisitesWereFulfilled()) {
            CarCharacteristicChooser.newInstance(characteristicFilter)
                    .show(getSupportFragmentManager(), "DIALOG");
        } else {
            Toast.makeText(this, R.string.select_previous_filter, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCharacteristicSelectedForFilter(CarCharacteristicFilter filter, CarCharacteristic characteristic) {
        // Translate serialized filter to real  filter instance.
        filter = mCarCharacteristicFilters.get(mCarCharacteristicFilters.indexOf(filter));
        if(filter != null) {
            filter.setSelectedCharacteristic(characteristic);
            mAdapter.notifyDataSetChanged();
        }
    }
}
