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
     * Whether there is still data to be loaded.
     */
    private boolean mHasMoreDataToLoad = true;

    /**
     * Listener to notify observers when a car characteristic was chosen.
     */
    private CarCharacteristicChooserListener mListener;

    /**
     * View type for loaded data.
     */
    private static final int VIEW_TYPE_DATA = 0;

    /**
     * View type for loading view.
     */
    private static final int VIEW_TYPE_LOADING = 1;

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
    public void setListener(CarCharacteristicChooserListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;
        if(viewType == VIEW_TYPE_DATA) {
            layout = R.layout.fragment_car_characteristic_item;
        } else {
            layout = R.layout.fragment_car_characteristic_item_loading;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_LOADING) {
            // Nothing to bind...
            return;
        }

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
        return mValues.size() + (mHasMoreDataToLoad ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mValues.size()) {
            return VIEW_TYPE_DATA;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    /**
     * Sets the items used by this adapter to render car characteristics.
     *
     * @param items The new loaded items.
     * @param hasMoreDataToLoad Whether there is still data to be loaded.
     */
    void setItems(List<CarCharacteristic> items, boolean hasMoreDataToLoad) {
        this.mValues = items;
        this.mHasMoreDataToLoad = hasMoreDataToLoad;
        notifyDataSetChanged();
    }
}
