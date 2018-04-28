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
import com.arecmetafora.interview.carrepository.api.CarCharacteristicFilter;
import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

public class CarCharacteristicFilterListAdapter extends RecyclerView.Adapter<CarCharacteristicFilterListAdapter.ViewHolder> {

    private List<CarCharacteristicFilter> mValues;
    private CarCharacteristicFilterListener mListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImage;
        final TextView mDescription;
        CarCharacteristicFilter mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = view.findViewById(R.id.car_characteristic_image);
            mDescription = view.findViewById(R.id.car_characteristic_description);
        }
    }

    public interface CarCharacteristicFilterListener {
        void onCharacteristicFilterOpened(CarCharacteristicFilter characteristicFilter);
    }

    @Inject
    CarCharacteristicFilterListAdapter(List<CarCharacteristicFilter> values) {
        this.mValues = values;
    }

    public void setListener(CarCharacteristicFilterListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_characteristic_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        CarCharacteristicFilter filter = mValues.get(position);
        holder.mItem = filter;

        holder.mImage.setVisibility(View.GONE);

        if(filter.getSelectedCharacteristic() == null) {
            holder.mDescription.setText(R.string.all_characteristics);
        } else {
            CarCharacteristic selectedCharacteristic = filter.getSelectedCharacteristic();
            holder.mDescription.setText(selectedCharacteristic.name);
            if(selectedCharacteristic.imageUrl != null) {
                Glide.with(holder.mImage)
                    .load(selectedCharacteristic.imageUrl)
                    .into(holder.mImage);
                holder.mImage.setVisibility(View.VISIBLE);
            }
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onCharacteristicFilterOpened(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
