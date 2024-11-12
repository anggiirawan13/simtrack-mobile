package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.user.AdminCreateUserActivity;
import com.simple.tracking.admin.adapter.UserAdapter;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.Objects;

public class UserFragment extends Fragment {

    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private CardView btnAddUser;
    private MaterialButton btnSearchUser;
    private TextInputEditText textInputSearchUser;

    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 10; // Adjust as needed

    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        recyclerView = view.findViewById(R.id.recyclerViewUser);
        btnAddUser = view.findViewById(R.id.btn_add_user);
        btnSearchUser = view.findViewById(R.id.btn_search_user);
        textInputSearchUser = view.findViewById(R.id.textInputSearchUser);

        btnAddUser.setOnClickListener(v -> {
            Intent userCreate = new Intent(requireContext(), AdminCreateUserActivity.class);
            userCreate.putExtra("MENU_NAME", "User");
            startActivity(userCreate);
        });

        btnSearchUser.setOnClickListener(v -> {
            currentPage = 1;
            isLastPage = false;
            isLoading = false;

            // Clear current data in the adapter before performing new search
            if (userAdapter != null) {
                userAdapter.clearUsers();
            }

            if (textInputSearchUser == null || textInputSearchUser.toString().trim().isEmpty()) {
                getUsers(null);
            } else {
                getUsers(Objects.requireNonNull(textInputSearchUser.getText()).toString());
            }
        });

        textInputSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    if (s.toString().isEmpty()) {
                        currentPage = 1;
                        isLastPage = false;
                        isLoading = false;

                        if (userAdapter != null) {
                            userAdapter.clearUsers();
                        }

                        getUsers(null); // Fetch without a query to get all users
                    }
                };

                searchHandler.postDelayed(searchRunnable, 1000); // 1-second delay
            }
        });

        // Fetch users from the API
        getUsers(null);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        getUsers(null); // Load the next page
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        currentPage = 1;
        isLastPage = false;
        isLoading = false;

        if (userAdapter != null) {
            userAdapter.clearUsers();
        }

        getUsers(null);
    }

    private void getUsers(String query) {
        if (isLoading || isLastPage) return; // Prevent duplicate requests

        isLoading = true;
        Call<BaseResponse<List<User>>> call = UserAPIConfiguration.getInstance().getUsers(query, true, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<BaseResponse<List<User>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<User>>> call, Response<BaseResponse<List<User>>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    BaseResponse<List<User>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        List<User> users = baseResponse.getData();

                        if (currentPage == 1) {
                            // For the first page, set up the adapter
                            userAdapter = new UserAdapter(users);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(userAdapter);
                        } else {
                            // Add new data to existing list on subsequent pages
                            userAdapter.addUsers(users);
                        }

                        // Check if this is the last page
                        if (users.size() < PAGE_SIZE) {
                            isLastPage = true;
                        } else {
                            currentPage++; // Load next page on next scroll
                        }
                    } else {
                        isLastPage = true; // No more data
                        Log.d("API Info", "No more users to load");
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<User>>> call, Throwable t) {
                isLoading = false;
                Log.e("API Error", "Failed to fetch users: " + t.getMessage());
            }
        });
    }
}
