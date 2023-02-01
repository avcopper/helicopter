package com.andrew.helicopter.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.R;
import com.andrew.helicopter.System.DataHandler;
import java.util.ArrayList;
import android.util.Log;

/**
 * Список деталей
 */
public class RecyclerAdapterDetails extends RecyclerView.Adapter<RecyclerAdapterDetails.RecyclerViewHolder> {
    private final ArrayList<Detail> details;
    private OnItemDetailClicked onDetailClick;

    public interface OnItemDetailClicked {
        void onItemDetailClick(int position);
    }

    public RecyclerAdapterDetails(ArrayList<Detail> details){
        this.details = details;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_detail_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String[] names = new String[details.size()];
        int[] globals = new int[details.size()];
        int[] globalBalances = new int[details.size()];
        int[] repairs = new int[details.size()];
        int[] repairBalances = new int[details.size()];
        long[] globalNexts = new long[details.size()];
        long[] globalPeriods = new long[details.size()];
        long[] repairNexts = new long[details.size()];
        long[] repairPeriods = new long[details.size()];

        for (int i = 0; i < details.size(); i++) {
            names[i] = details.get(i).getName();
            globals[i] = details.get(i).getResourceGlobal();
            globalBalances[i] = details.get(i).getResourceGlobalBalance();
            repairs[i] = details.get(i).getResourceRepair();
            repairBalances[i] = details.get(i).getResourceRepairBalance();
            globalNexts[i] = details.get(i).getResourceGlobalNext();
            globalPeriods[i] = details.get(i).getResourceGlobalPeriod();
            repairNexts[i] = details.get(i).getResourceRepairNext();
            repairPeriods[i] = details.get(i).getResourceRepairPeriod();
        }

        holder.setData(holder.getAdapterPosition(), names,
                globals, globalBalances,
                repairs, repairBalances,
                globalNexts, globalPeriods,
                repairNexts, repairPeriods);

        holder.nameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailClick.onItemDetailClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public void setOnDetailClick(OnItemDetailClicked onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameContainer;
        private final TextView globalBalanceContainer;
        private final TextView repairBalanceContainer;
        private final TextView globalNextContainer;
        private final TextView repairNextContainer;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameContainer = itemView.findViewById(R.id.recycler_detail_name);
            globalBalanceContainer = itemView.findViewById(R.id.recycler_detail_global_balance);
            repairBalanceContainer = itemView.findViewById(R.id.recycler_detail_repair_balance);
            globalNextContainer = itemView.findViewById(R.id.recycler_detail_global_next);
            repairNextContainer = itemView.findViewById(R.id.recycler_detail_repair_next);
        }

        void setData(int position, String[] names,
                     int[] globals, int[] globalBalances,
                     int[] repairs, int[] repairBalances,
                     long[] globalNexts, long[] globalPeriods,
                     long[] repairNexts, long[] repairPeriods) {

            long currenTimestamp = DataHandler.getCurrentUnixTimestamp();

            nameContainer.setText(names[position]);
            String global = DataHandler.simpleFormatDateFromMinutes(globalBalances[position]);
            String repair = DataHandler.simpleFormatDateFromMinutes(repairBalances[position]);
            String gNext = DataHandler.getDateFromUnixTimestamp(globalNexts[position]);
            String rNext = DataHandler.getDateFromUnixTimestamp(repairNexts[position]);
            // остаток до замена в часах
            if (globals[position] > 0) {
                if (globalBalances[position] <= Detail.LIMIT_HOURS)
                    globalBalanceContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 50 ч.
                else globalBalanceContainer.setBackgroundResource(android.R.color.transparent);
                globalBalanceContainer.setText(global);
            }
            else {
                globalBalanceContainer.setText("");
                globalBalanceContainer.setBackgroundResource(android.R.color.transparent);
            }
            // остаток до ремонта в часах
            if (repairs[position] > 0) {
                if (repairBalances[position] <= Detail.LIMIT_HOURS)
                    repairBalanceContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 50 ч
                else repairBalanceContainer.setBackgroundResource(android.R.color.transparent);
                repairBalanceContainer.setText(repair);
            }
            else {
                repairBalanceContainer.setText("");
                repairBalanceContainer.setBackgroundResource(android.R.color.transparent);
            }
            // дата замены
            if (globalPeriods[position] > 0) {
                if (globalNexts[position] - currenTimestamp <= Detail.LIMIT_MONTHS)
                    globalNextContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 6 мес.
                else globalNextContainer.setBackgroundResource(android.R.color.transparent);
                globalNextContainer.setText(gNext);
            }
            else {
                globalNextContainer.setText("");
                globalNextContainer.setBackgroundResource(android.R.color.transparent);
            }
            // дата ремонта
            if (repairPeriods[position] > 0) {
                if (repairNexts[position] - currenTimestamp <= Detail.LIMIT_MONTHS)
                    repairNextContainer.setBackgroundResource(R.color.ripple_button_background); // осталось меньше 6 мес.
                else repairNextContainer.setBackgroundResource(android.R.color.transparent);
                repairNextContainer.setText(rNext);
            }
            else {
                repairNextContainer.setText("");
                repairNextContainer.setBackgroundResource(android.R.color.transparent);
            }
        }
    }
}
