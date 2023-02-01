package com.andrew.helicopter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Time;
import com.andrew.helicopter.Recycler.RecyclerAdapterTimes;
import com.andrew.helicopter.System.DataHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TimeListActivity extends BaseActivity implements RecyclerAdapterTimes.OnItemTimesClicked {
    private static final String TABLE = "times";

    LinearLayout messageMain;
    TextView messageText;
    Toolbar toolbar;
    Spinner spinnerMonths, spinnerYears;
    Button timesSearch, addTime;
    ArrayList<Time> times = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);

        helicopter = (Helicopter) getIntent().getSerializableExtra("HELICOPTER");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.title));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        messageMain = findViewById(R.id.message_main);
        messageText = findViewById(R.id.message_text);

        if (helicopter == null || helicopter.getNumber() == null) {
            messageText.setText("Номер вертолета не определен.\nВыберите борт на главном экране");
            messageMain.setVisibility(View.VISIBLE);
        } else {
            messageMain.setVisibility(View.INVISIBLE);
            setTitle(actionBar, helicopter.getNumber());

            init();
            setListeners();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM.yyyy", Locale.ENGLISH);
            String dt = formatDate.format(date);
            getTimeList(dt);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Клик по выбранному пункты работ
     * @param position - позиция выбранного пункта в массиве работ
     */
    @Override
    public void onItemTimesClick(int position) {
        if (helicopter != null && times != null) {
            Time time = times.get(position);

            Intent intent = new Intent(this, TimeActivity.class);
            intent.putExtra("HELICOPTER", helicopter) ;
            intent.putExtra("TIME", time);
            intent.putExtra("CURRENT_USER", currentUser);
            startActivity(intent);
        }
    }

    /**
     * Инициализация элементов
     */
    private void init() {
        spinnerMonths = findViewById(R.id.months);
        spinnerYears = findViewById(R.id.years);
        timesSearch = findViewById(R.id.times_search);
        addTime = findViewById(R.id.add_time);
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // поиск списка работ в выбранном месяце и году
        timesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedMonth = spinnerMonths.getSelectedItem().toString();
                String selectedYear = spinnerYears.getSelectedItem().toString();
                getTimeList(DataHandler.getMonth(selectedMonth) + "." + selectedYear);
            }
        });
        // добавление новой работы
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helicopter != null) {
                    Intent intent = new Intent(TimeListActivity.this, TimeActivity.class);
                    intent.putExtra("HELICOPTER", helicopter);
                    intent.putExtra("TIME", new Time());
                    intent.putExtra("CURRENT_USER", currentUser);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Возвращает список работ в выбранном месяце
     * @param month - дата в формате месяц.год (dd.MM)
     */
    private void getTimeList(String month) {
        db.collection(TABLE + "/" + helicopter.getNumber() + "/" + month)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        times.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Time item = document.toObject(Time.class);
                            times.add(item);
                        }
                        showTimes(times);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Вывод списка произведенных работ
     * @param times - массив работ
     */
    private  void showTimes(ArrayList<Time> times) {
        RecyclerView recyclerViewTime = findViewById(R.id.recycler_view_times);
        recyclerViewTime.setHasFixedSize(true);
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerAdapterTimes rList = new RecyclerAdapterTimes(times);
        recyclerViewTime.setAdapter(rList);
        rList.setOnTimeClick(this);
    }
}
