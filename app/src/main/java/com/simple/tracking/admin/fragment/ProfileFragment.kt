package com.simple.tracking.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.simple.tracking.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)

        val roles = arrayOf("Admin", "Commissioner", "Director", "Shipper")

        val roleSpinner = view.findViewById<MaterialAutoCompleteTextView>(R.id.roleSpinner)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        roleSpinner.setAdapter(adapter)

        roleSpinner.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                roleSpinner.showDropDown()
            }
        }

        roleSpinner.setOnClickListener {
            roleSpinner.showDropDown()
        }

        return view
    }
}