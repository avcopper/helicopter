package com.andrew.helicopter.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.User;
import com.andrew.helicopter.R;

import java.util.ArrayList;

/**
 * Список пользователей
 */
public class RecyclerAdapterUsers extends RecyclerView.Adapter<RecyclerAdapterUsers.RecyclerViewHolder> {
    private final ArrayList<User> users;
    private OnUserClicked onClick;

    public interface OnUserClicked {
        void onUserClick(int position);
    }

    public RecyclerAdapterUsers(ArrayList<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String[] logins = new String[users.size()];
        String[] emails = new String[users.size()];
        String[] roles = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            logins[i] = users.get(i).getLogin();
            emails[i] = users.get(i).getEmail();
            roles[i] = users.get(i).getRole();
        }

        holder.setData(holder.getAdapterPosition(), logins, emails, roles);

        holder.loginContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onUserClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnUserClick(OnUserClicked onClick) {
        this.onClick = onClick;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView loginContainer;
        private final TextView emailContainer;
        private final TextView roleContainer;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            loginContainer = itemView.findViewById(R.id.recycler_user_login);
            emailContainer = itemView.findViewById(R.id.recycler_user_email);
            roleContainer = itemView.findViewById(R.id.recycler_user_role);
        }

        void setData(int position, String[] logins, String[] emails, String[] roles) {
            loginContainer.setText(logins[position]);
            emailContainer.setText(emails[position]);
            roleContainer.setText(roles[position]);
        }
    }
}
