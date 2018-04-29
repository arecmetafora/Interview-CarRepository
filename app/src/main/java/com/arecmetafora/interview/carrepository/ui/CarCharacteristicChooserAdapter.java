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
import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter used to render the list of car characteristics to be chosen.
 */
public class CarCharacteristicChooserAdapter extends RecyclerView.Adapter<CarCharacteristicChooserAdapter.ViewHolder> {

    /**
     * List of all loaded car characteristics.
     */
    private List<CarCharacteristic> mValues;

    /**
     * Listener to notify observers when a car characteristic was chosen.
     */
    private CarCharacteristicChooser.CarCharacteristicChooserListener mListener;

    /**
     * ViewHolder for this adapter.
     */
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

    /**
     * Sets a listener for this adapter.
     * @param mListener Listener to notify observers when a car characteristic was chosen.
     */
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
        CarCharacteristic characteristic = mValues.get(position);
        holder.mItem = characteristic;

        holder.mDescription.setText(characteristic.name);

        holder.mImage.setVisibility(View.GONE);

        if(characteristic.imageUrl != null) {
            Glide.with(holder.mImage)
                    .load(characteristic.imageUrl)
                    .into(holder.mImage);
            holder.mImage.setVisibility(View.VISIBLE);
        }

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

    /**
     * Sets the items used by this adapter to render car characteristics.
     *
     * @param items The new loaded items.
     */
    void setItems(List<CarCharacteristic> items) {
        this.mValues = items;
        notifyDataSetChanged();
    }
}
