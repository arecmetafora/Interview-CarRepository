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
public class CarCharacteristicChooser extends DaggerAppCompatDialogFragment
        implements CarCharacteristicChooserAdapter.CarCharacteristicChooserListener {

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
     * Listener to notify observers when a car characteristic was chosen for a given filter.
     */
    private CarCharacteristicChooserListener mListener;

    /**
     * Listener to notify events about car characteristic choices.
     */
    public interface CarCharacteristicChooserListener {
        /**
         * Called when a car characteristic was chosen.
         *
         * @param filter Filter used by the car characteristic chooser.
         * @param characteristic The chosen car characteristic.
         */
        void onCharacteristicSelectedForFilter(CarCharacteristicFilter filter, CarCharacteristic characteristic);
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
                        .observe(this, characteristics -> {
                            if(characteristics != null) {
                                mAdapter.setItems(characteristics);
                            } else if(getView() != null) {
                                getView().post(() -> mAdapter.noMoreDataToLoad());
                            }
                        });
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

        mAdapter.setListener(this);
        list.setAdapter(mAdapter);

        // Adds scroll listener to load for data
        // TODO: Abstract this implementation to a class that extends ReciclerView and use it!
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(mAdapter.hasMoreDataToLoad()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        ViewModelProviders.of(CarCharacteristicChooser.this)
                                .get(CarCharacteristicsViewModel.class)
                                .loadMoreCharacteristics(mFilter);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        assert getDialog().getWindow() != null;
        getDialog().setTitle(mFilter.getFilterDescription());
        getDialog().getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CarCharacteristicChooserListener) {
            mListener = (CarCharacteristicChooserListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCharacteristicSelected(CarCharacteristic characteristic) {
        if(mListener != null) {
            mListener.onCharacteristicSelectedForFilter(mFilter, characteristic);
        }
        getDialog().dismiss();
    }

    @Override
    public void loadMoreData() {
        ViewModelProviders.of(CarCharacteristicChooser.this)
                .get(CarCharacteristicsViewModel.class)
                .loadMoreCharacteristics(mFilter);
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
