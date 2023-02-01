package com.andrew.helicopter.Recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrew.helicopter.MainActivity;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Список вертолетов
 */
public class RecyclerAdapterList extends RecyclerView.Adapter<RecyclerAdapterList.RecyclerViewHolder> {
    private final ArrayList<Helicopter> helicopters;
    private OnItemClicked onClick;
    private OnItemLongClicked onLongClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public interface OnItemLongClicked {
        void onItemLongClick(int position);
    }

    public RecyclerAdapterList(ArrayList<Helicopter> helicopters){
        this.helicopters = helicopters;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_helicopter_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String[] numbers = new String[helicopters.size()];

        for (int i = 0; i < helicopters.size(); i++) {
            numbers[i] = helicopters.get(i).getNumber();
        }

        holder.setData(holder.getAdapterPosition(), numbers);

        holder.numberContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

        holder.numberContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClick.onItemLongClick(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return helicopters.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    public void setOnLongClick(OnItemLongClicked onLongClick) {
        this.onLongClick = onLongClick;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView numberContainer;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            numberContainer = itemView.findViewById(R.id.recycler_helicopter_item);
        }

        void setData(int position, String[] helicopters) {
            numberContainer.setText(helicopters[position]);
        }
    }
}
