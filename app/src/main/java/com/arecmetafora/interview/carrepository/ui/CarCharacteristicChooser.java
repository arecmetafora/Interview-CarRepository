package com.arecmetafora.interview.carrepository.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;
import com.arecmetafora.interview.carrepository.api.CarCharacteristicsViewModel;
import com.arecmetafora.interview.carrepository.di.ActivityScoped;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * Fragment used to choose a car characteristic for a given filter.
 */
@ActivityScoped
public class CarCharacteristicChooser extends DaggerAppCompatDialogFragment {

    /**
     * Bundle argument for the filter used by this characteristic filter.
     */
    private static final String ARG_CHARACTERISTIC_FILTER = "CHARACTERISTIC_FILTER";

    /**
     * Filter used by the car characteristic chooser.
     */
    private CarCharacteristicFilter mFilter;

    @Inject
    CarCharacteristicChooserAdapter mAdapter;

    /**
     * Listener to notify events about car characteristic choices.
     */
    public interface CarCharacteristicChooserListener {
        /**
         * Called when a car characteristic was chosen.
         *
         * @param characteristic The chosen car characteristic.
         */
        void onCharacteristicSelected(CarCharacteristic characteristic);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DaggerAppCompatDialogFragment.STYLE_NORMAL, R.style.CharacteristicChooserTheme);

        if(getArguments() != null) {
            mFilter = (CarCharacteristicFilter) getArguments()
                    .getSerializable(ARG_CHARACTERISTIC_FILTER);

            if(mFilter != null) {
                ViewModelProviders.of(this)
                        .get(CarCharacteristicsViewModel.class)
                        .getCharacteristics(mFilter)
                        .observe(this,
                                characteristics -> mAdapter.setItems(characteristics));
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_characteristic_list, container, false);

        assert getContext() != null;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView list = view.findViewById(R.id.car_characteristic_list);
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        list.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        assert getDialog().getWindow() != null;
        getDialog().setTitle(mFilter.getChooserTitle());
        getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
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

    /**
     * Creates a new car characteristic chooser for a given filter.
     *
     * @param filter The filter to be used y the car characteristic chooser.
     * @return The fragment which contains the car characteristic chooser interface.
     */
    static CarCharacteristicChooser newInstance(CarCharacteristicFilter filter) {
        CarCharacteristicChooser characteristicChooser = new CarCharacteristicChooser();
        characteristicChooser.setShowsDialog(true);

        Bundle args = new Bundle();
        args.putSerializable(ARG_CHARACTERISTIC_FILTER, filter);
        characteristicChooser.setArguments(args);

        return characteristicChooser;
    }
}
