package com.simple.tracking.admin.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.R
import com.simple.tracking.admin.activity.user.AdminCreateUserActivity
import com.simple.tracking.admin.adapter.UserAdapter
import com.simple.tracking.model.User
import com.simple.tracking.network.BaseResponse
import com.simple.tracking.network.UserAPIConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddUser: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_user, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnAddUser = view.findViewById(R.id.btn_add_user)

        btnAddUser.setOnClickListener {
            val userCreate = Intent(requireContext(), AdminCreateUserActivity::class.java)
            userCreate.putExtra("MENU_NAME", "User")
            startActivity(userCreate)
        }

        // Ambil data pengguna dari API
        fetchUsers()

        return view
    }

    private fun fetchUsers() {
        val call = UserAPIConfiguration.instance.getUsers()
        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { baseResponse ->
                        if (baseResponse.success) {
                            // Memperbarui daftar pengguna di adapter
                            if (baseResponse.data.isEmpty()) {
                                Log.d("API Info", "No users found")
                                Toast.makeText(context, "No users found", Toast.LENGTH_SHORT).show()
                            } else {
                                userAdapter = UserAdapter(baseResponse.data)
                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.adapter = userAdapter
                            }
                        } else {
                            Log.e("API Error", "API call was not successful: ${baseResponse.messages}")
                        }
                    }
                } else {
                    Log.e("API Error", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch users: ${t.message}")
            }
        })
    }
}