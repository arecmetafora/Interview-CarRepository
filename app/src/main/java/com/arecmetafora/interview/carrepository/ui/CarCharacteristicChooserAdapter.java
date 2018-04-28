package com.arecmetafora.interview.carrepository.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arecmetafora.interview.carrepository.R;
import com.arecmetafora.interview.carrepository.api.CarCharacteristic;
import com.arecmetafora.interview.carrepository.di.FragmentScoped;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class CarCharacteristicChooserAdapter extends RecyclerView.Adapter<CarCharacteristicChooserAdapter.ViewHolder> {

    private List<CarCharacteristic> mValues;
    private CarCharacteristicChooser.CarCharacteristicChooserListener mListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImage;
        final TextView mDescription;
        CarCharacteristic mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = view.findViewById(R.id.car_characteristic_image);
            mDescription = view.findViewById(R.id.car_characteristic_description);
        }
    }

    @Inject
    CarCharacteristicChooserAdapter() {
        this.mValues = new LinkedList<>();
    }

    public void setListener(CarCharacteristicChooser.CarCharacteristicChooserListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_car_characteristic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDescription.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onCharacteristicSelected(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    void setItems(List<CarCharacteristic> items) {
        this.mValues = items;
        notifyDataSetChanged();
    }
}
