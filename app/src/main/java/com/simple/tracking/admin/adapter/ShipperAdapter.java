package com.simple.tracking.admin.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.shipper.AdminViewShipperActivity;
import com.simple.tracking.model.Shipper;
import java.util.List;

public class ShipperAdapter extends RecyclerView.Adapter<ShipperAdapter.ShipperViewHolder> {

    private final List<Shipper> shipperList;

    public ShipperAdapter(List<Shipper> shipperList) {
        this.shipperList = shipperList;
    }

    public static class ShipperViewHolder extends RecyclerView.ViewHolder {
        public final TextView numberText;
        public final TextView titleText;
        public final TextView subtitleText;

        public ShipperViewHolder(View view) {
            super(view);
            numberText = view.findViewById(R.id.numberText);
            titleText = view.findViewById(R.id.titleText);
            subtitleText = view.findViewById(R.id.subtitleText);
        }
    }

    @Override
    public ShipperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_shipper, parent, false);
        return new ShipperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShipperViewHolder holder, int position) {
        Shipper shipper = shipperList.get(position);
        holder.numberText.setText(String.valueOf(position + 1));
        holder.titleText.setText(shipper.getUser().getFullname());
        holder.subtitleText.setText(shipper.getDeviceMapping());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.light_green));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    android.R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AdminViewShipperActivity.class);
            intent.putExtra("SHIPPER_ID", shipper.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return shipperList.size();
    }
}
