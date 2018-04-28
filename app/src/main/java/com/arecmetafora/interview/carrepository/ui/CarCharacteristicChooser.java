package com.arecmetafora.interview.carrepository.ui;

import android.app.ActionBar;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicType;
import com.arecmetafora.interview.carrepository.di.ActivityScoped;
import com.arecmetafora.interview.carrepository.di.FragmentScoped;

import javax.inject.Inject;

import dagger.android.DaggerDialogFragment;
import dagger.android.support.DaggerAppCompatDialogFragment;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class CarCharacteristicChooser extends DaggerAppCompatDialogFragment {

    private static final String ARG_CHARACTERISTIC_TYPE = "CHARACTERISTIC_TYPE";
    private CarCharacteristicType characteristicType;

    @Inject
    CarCharacteristicChooserAdapter mAdapter;

    public interface CarCharacteristicChooserListener {
        void onCharacteristicSelected(CarCharacteristic characteristic);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            characteristicType = (CarCharacteristicType) getArguments().getSerializable(ARG_CHARACTERISTIC_TYPE);
        }

        ViewModelProviders.of(this)
                .get(CarCharacteristicsViewModel.class)
                .getCharacteristics()
                .observe(this, characteristics -> mAdapter.setItems(characteristics));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_characteristic_list, container, false);
        RecyclerView list = view.findViewById(R.id.car_characteristic_list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CarCharacteristicChooserListener) {
            mAdapter.setListener((CarCharacteristicChooserListener) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    static CarCharacteristicChooser newInstance(CarCharacteristicType type) {
        CarCharacteristicChooser characteristicChooser = new CarCharacteristicChooser();
        characteristicChooser.setShowsDialog(true);

        Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTERISTIC_TYPE, type);
        characteristicChooser.setArguments(args);

        return characteristicChooser;
    }
}
