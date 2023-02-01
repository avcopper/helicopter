package com.andrew.helicopter.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Time;
import com.andrew.helicopter.R;
import com.andrew.helicopter.System.DataHandler;
import java.util.ArrayList;

/**
 * Список рабочего времени
 */
public class RecyclerAdapterTimes extends RecyclerView.Adapter<RecyclerAdapterTimes.RecyclerViewHolder> {
    private final ArrayList<Time> times;
    private OnItemTimesClicked onTimeClick;

    public interface OnItemTimesClicked {
        void onItemTimesClick(int position);
    }

    public RecyclerAdapterTimes(ArrayList<Time> times){
        this.times = times;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_time_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String[] dates = new String[times.size()];
        int[] airs = new int[times.size()];
        int[] earths = new int[times.size()];

        for (int i = 0; i < times.size(); i++) {
            dates[i] = times.get(i).getDate();
            airs[i] = times.get(i).getAir();
            earths[i] = times.get(i).getEarth();
        }

        holder.setData(holder.getAdapterPosition(), dates, airs, earths);

        holder.dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeClick.onItemTimesClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public void setOnTimeClick(OnItemTimesClicked onTimeClick) {
        this.onTimeClick = onTimeClick;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateContainer;
        private final TextView airContainer;
        private final TextView earthContainer;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            dateContainer = itemView.findViewById(R.id.recycler_time_date);
            airContainer = itemView.findViewById(R.id.recycler_time_air);
            earthContainer = itemView.findViewById(R.id.recycler_time_earth);
        }

        void setData(int position, String[] dates, int[] airs, int[] earths)
        {
            dateContainer.setText(dates[position]);
            String air = DataHandler.simpleFormatDateFromMinutes(airs[position]);
            String earth = DataHandler.simpleFormatDateFromMinutes(earths[position]);

            airContainer.setText(air);
            earthContainer.setText(earth);
        }
    }
}
