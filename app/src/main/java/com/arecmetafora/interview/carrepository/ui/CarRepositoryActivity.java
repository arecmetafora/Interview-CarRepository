package com.arecmetafora.interview.carrepository.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicType;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class CarRepositoryActivity extends DaggerAppCompatActivity
        implements CarCharacteristicChooser.CarCharacteristicChooserListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_repository);

        TextView carManufacturer = findViewById(R.id.car_manufacturer);
        carManufacturer.setText(R.string.all_characteristics);
    }

    public void onChangeCarManufacturer(View view) {
        CarCharacteristicChooser.newInstance(CarCharacteristicType.MANUFACTURER)
                .show(getSupportFragmentManager(), "DIALOG");
    }

    @Override
    public void onCharacteristicSelected(CarCharacteristic characteristic) {

    }
}
