package com.andrew.helicopter.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Work;
import com.andrew.helicopter.R;
import com.andrew.helicopter.System.DataHandler;
import java.util.ArrayList;
import android.util.Log;

/**
 * Список работ
 */
public class RecyclerAdapterWorks extends RecyclerView.Adapter<RecyclerAdapterWorks.RecyclerViewHolder> {
    private final ArrayList<Work> works;
    private OnItemWorkClicked onWorkClick;

    public interface OnItemWorkClicked {
        void onItemWorkClick(int position);
    }

    public RecyclerAdapterWorks(ArrayList<Work> works){
        this.works = works;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_work_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String[] names = new String[works.size()];
        int[] hours = new int[works.size()];
        int[] balances = new int[works.size()];
        long[] months = new long[works.size()];
        long[] nextWorks = new long[works.size()];

        for (int i = 0; i < works.size(); i++) {
            names[i] = works.get(i).getName();
            hours[i] = works.get(i).getResourceHour();
            balances[i] = works.get(i).getResourceHourBalance();
            months[i] = works.get(i).getResourceMonth();
            nextWorks[i] = works.get(i).getWorkDateNext();
        }

        holder.setData(holder.getAdapterPosition(), names, hours, balances, months, nextWorks);

        holder.nameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkClick.onItemWorkClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public void setOnWorkClick(OnItemWorkClicked onWorkClick) {
        this.onWorkClick = onWorkClick;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameContainer;
        private final TextView balanceContainer;
        private final TextView nextContainer;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameContainer = itemView.findViewById(R.id.recycler_work_name);
            balanceContainer = itemView.findViewById(R.id.recycler_work_balance);
            nextContainer = itemView.findViewById(R.id.recycler_work_next);
        }

        void setData(int position, String[] names, int[] hours, int[] balances, long[] months, long[] works)
        {
            nameContainer.setText(names[position]);
            //
            if (hours[position] > 0) {
                if ((hours[position] <= 1500 && balances[position] <= Work.LIMIT_SHORT) ||
                        (hours[position] > 1500 && balances[position] <= Work.LIMIT_LONG))
                    balanceContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 5-10 ч.
                else balanceContainer.setBackgroundResource(android.R.color.transparent);
                balanceContainer.setText(DataHandler.simpleFormatDateFromMinutes(balances[position]));
            } else {
                balanceContainer.setText("");
                balanceContainer.setBackgroundResource(android.R.color.transparent);
            }
            //
            if (months[position] > 0) {
                if (works[position] <= (DataHandler.getCurrentUnixTimestamp() + Work.LIMIT_MONTH))
                    nextContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 7 дн.
                else nextContainer.setBackgroundResource(android.R.color.transparent);
                nextContainer.setText(DataHandler.getDateFromUnixTimestamp(works[position]));
            } else {
                nextContainer.setText("");
                nextContainer.setBackgroundResource(android.R.color.transparent);
            }

        }
    }
}
