package com.simple.tracking.admin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.admin.adapter.DeliveryAdapter
import com.simple.tracking.R
import com.simple.tracking.admin.activity.delivery.AdminCreateDeliveryDetailActivity
import com.simple.tracking.admin.activity.user.AdminCreateUserActivity
import com.simple.tracking.model.Delivery

class DeliveryFragment : Fragment() {

    private lateinit var deliveryAdapter: DeliveryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddDelivery: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_delivery, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnAddDelivery = view.findViewById(R.id.btn_add_delivery)

        btnAddDelivery.setOnClickListener {
            val userCreate = Intent(requireContext(), AdminCreateDeliveryDetailActivity::class.java)
            userCreate.putExtra("MENU_NAME", "User")
            startActivity(userCreate)
        }

        val deliveryList = listOf(
            Delivery("Company Name 1", "Address Company Name 1"),
            Delivery("Company Name 2", "Address Company Name 2"),
            Delivery("Company Name 3", "Address Company Name 3"),
            Delivery("Company Name 4", "Address Company Name 4"),
            Delivery("Company Name 5", "Address Company Name 5"),
            Delivery("Company Name 6", "Address Company Name 6"),
            Delivery("Company Name 7", "Address Company Name 7"),
            Delivery("Company Name 8", "Address Company Name 8"),
            Delivery("Company Name 9", "Address Company Name 9"),
            Delivery("Company Name 10", "Address Company Name 10"),
            Delivery("Company Name 11", "Address Company Name 11"),
            Delivery("Company Name 12", "Address Company Name 12"),
            Delivery("Company Name 13", "Address Company Name 13"),
            Delivery("Company Name 14", "Address Company Name 14")
        )

        deliveryAdapter = DeliveryAdapter(deliveryList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = deliveryAdapter

        return view
    }
}