package com.simple.tracking.admin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.R
import com.simple.tracking.admin.activity.delivery.AdminViewDeliveryDetailActivity
import com.simple.tracking.admin.activity.user.AdminViewUserActivity
import com.simple.tracking.model.Delivery

class DeliveryAdapter(private val deliveryList: List<Delivery>) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    inner class DeliveryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberText: TextView = view.findViewById(R.id.numberText)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val subtitleText: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_delivery, parent, false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val delivery = deliveryList[position]
        holder.numberText.text = (position + 1).toString()
        holder.titleText.text = delivery.name
        holder.subtitleText.text = delivery.address

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.light_green
            ))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminViewDeliveryDetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return deliveryList.size
    }
}

