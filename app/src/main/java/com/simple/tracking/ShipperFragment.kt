package com.simple.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.model.Shipper

class ShipperFragment : Fragment() {

    private lateinit var shipperAdapter: ShipperAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shipper, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        val shipperList = listOf(
            Shipper("Shipper 1", "Device Mapping Shipper 1"),
            Shipper("Shipper 2", "Device Mapping Shipper 2"),
            Shipper("Shipper 3", "Device Mapping Shipper 3"),
            Shipper("Shipper 4", "Device Mapping Shipper 4"),
            Shipper("Shipper 5", "Device Mapping Shipper 5"),
            Shipper("Shipper 6", "Device Mapping Shipper 6"),
            Shipper("Shipper 7", "Device Mapping Shipper 7"),
            Shipper("Shipper 8", "Device Mapping Shipper 8"),
            Shipper("Shipper 9", "Device Mapping Shipper 9"),
            Shipper("Shipper 10", "Device Mapping Shipper 10"),
            Shipper("Shipper 11", "Device Mapping Shipper 11"),
            Shipper("Shipper 12", "Device Mapping Shipper 12"),
            Shipper("Shipper 13", "Device Mapping Shipper 13"),
            Shipper("Shipper 14", "Device Mapping Shipper 14")
        )

        shipperAdapter = ShipperAdapter(shipperList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = shipperAdapter

        return view
    }
}
