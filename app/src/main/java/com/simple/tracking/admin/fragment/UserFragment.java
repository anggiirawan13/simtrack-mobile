package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class UserFragment extends Fragment {

    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private CardView btnAddUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUser);
        btnAddUser = view.findViewById(R.id.btn_add_user);

        btnAddUser.setOnClickListener(v -> {
            Intent userCreate = new Intent(requireContext(), AdminCreateUserActivity.class);
            userCreate.putExtra("MENU_NAME", "User");
            startActivity(userCreate);
        });

        // Fetch users from the API
        getUsers();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers(); // Fetch users whenever the fragment is resumed
    }

    private void getUsers() {
        Call<BaseResponse<List<User>>> call = UserAPIConfiguration.getInstance().getUsers();
        call.enqueue(new Callback<BaseResponse<List<User>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<User>>> call, Response<BaseResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<User>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        if (baseResponse.getData().isEmpty()) {
                            Log.d("API Info", "No users found");
                            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                        } else {
                            userAdapter = new UserAdapter(baseResponse.getData());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(userAdapter);
                        }
                    } else {
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<User>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch users: " + t.getMessage());
            }
        });
    }
}
