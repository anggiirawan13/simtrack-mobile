package com.simple.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.model.User

class UserFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        val userList = listOf(
            User("User 1", "Address User 1"),
            User("User 2", "Address User 2"),
            User("User 3", "Address User 3"),
            User("User 4", "Address User 4"),
            User("User 5", "Address User 5"),
            User("User 6", "Address User 6"),
            User("User 7", "Address User 7"),
            User("User 8", "Address User 8"),
            User("User 9", "Address User 9"),
            User("User 10", "Address User 10"),
            User("User 11", "Address User 11"),
            User("User 12", "Address User 12"),
            User("User 13", "Address User 13"),
            User("User 14", "Address User 14")
        )

        userAdapter = UserAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = userAdapter

        return view
    }
}
