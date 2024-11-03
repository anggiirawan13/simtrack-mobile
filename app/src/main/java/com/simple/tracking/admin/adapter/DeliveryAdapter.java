package com.simple.tracking.admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.delivery.AdminViewDeliveryDetailActivity;
import com.simple.tracking.model.Delivery;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private final List<Delivery> deliveryList;

    public DeliveryAdapter(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        public final TextView numberText;
        public final TextView titleText;
        public final TextView subtitleText;

        public DeliveryViewHolder(View view) {
            super(view);
            numberText = view.findViewById(R.id.numberText);
            titleText = view.findViewById(R.id.titleText);
            subtitleText = view.findViewById(R.id.subtitleText);
        }
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.numberText.setText(String.valueOf(position + 1));
        holder.titleText.setText(delivery.getDeliveryNumber());
        holder.subtitleText.setText(delivery.getCompanyName());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.light_green));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    android.R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AdminViewDeliveryDetailActivity.class);
            intent.putExtra("DELIVERY_ID", delivery.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }
}
