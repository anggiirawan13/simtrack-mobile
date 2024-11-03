package com.simple.tracking.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.simple.tracking.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        String[] roles = {"Admin", "Commissioner", "Director", "Shipper"};

        MaterialAutoCompleteTextView roleSpinner = view.findViewById(R.id.roleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, roles);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                roleSpinner.showDropDown();
            }
        });

        roleSpinner.setOnClickListener(v -> roleSpinner.showDropDown());

        return view;
    }
}
