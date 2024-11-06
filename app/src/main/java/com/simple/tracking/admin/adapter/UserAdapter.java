package com.simple.tracking.admin.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.user.AdminViewUserActivity;
import com.simple.tracking.model.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public final TextView numberText;
        public final TextView titleText;
        public final TextView subtitleText;

        public UserViewHolder(View view) {
            super(view);
            numberText = view.findViewById(R.id.numberText);
            titleText = view.findViewById(R.id.titleText);
            subtitleText = view.findViewById(R.id.subtitleText);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.numberText.setText(String.valueOf(position + 1));
        holder.titleText.setText(user.getFullname());
        holder.subtitleText.setText(user.getUsername());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.light_green));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    android.R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AdminViewUserActivity.class);
            intent.putExtra("USER_ID", user.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUsers(List<User> newUsers) {
        int previousSize = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(previousSize, newUsers.size());
    }

    public void clearUsers() {
        if (userList != null) {
            userList.clear();
            notifyDataSetChanged();
        }
    }

}
