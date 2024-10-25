package com.simple.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.model.Shipper

class ShipperAdapter(private val shipperList: List<Shipper>) : RecyclerView.Adapter<ShipperAdapter.ShipperViewHolder>() {

    inner class ShipperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberText: TextView = view.findViewById(R.id.numberText)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val subtitleText: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shipper, parent, false)
        return ShipperViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipperViewHolder, position: Int) {
        val shipper = shipperList[position]
        holder.numberText.text = (position + 1).toString()
        holder.titleText.text = shipper.name
        holder.subtitleText.text = shipper.deviceMapping

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.light_green))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return shipperList.size
    }
}

