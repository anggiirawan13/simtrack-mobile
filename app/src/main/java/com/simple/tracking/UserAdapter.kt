package com.simple.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.simple.tracking.model.User

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numberText: TextView = view.findViewById(R.id.numberText)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val subtitleText: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.numberText.text = (position + 1).toString()
        holder.titleText.text = user.name
        holder.subtitleText.text = user.deviceMapping

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.light_green))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

