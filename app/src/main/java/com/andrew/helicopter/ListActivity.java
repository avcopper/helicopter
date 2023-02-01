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
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Recycler.RecyclerAdapterDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class ListActivity extends BaseActivity implements RecyclerAdapterDetails.OnItemDetailClicked {
    LinearLayout messageMain;
    TextView messageText;
    Toolbar toolbar;
    Spinner spinnerGroups;
    Button groupSearch;
    ArrayList<Detail> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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
            getDetailList();
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
     * Клик по детали выводит полную инфу о ней
     * @param position - номер позиции выбранной детали в массиве
     */
    @Override
    public void onItemDetailClick(int position) {
        if (helicopter != null && details != null) {
            Detail detail = details.get(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("HELICOPTER", helicopter) ;
            intent.putExtra("DETAIL", detail);
            intent.putExtra("CURRENT_USER", currentUser);
            startActivity(intent);
        }
    }

    /**
     * Инициализация элементов
     */
    private void init() {
        spinnerGroups = findViewById(R.id.groups);
        groupSearch = findViewById(R.id.group_search);
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // поиск списка деталей по группе
        groupSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected = spinnerGroups.getSelectedItem().toString();
                if (selected.equals("Все")) getDetailList();
                else getDetailListByGroup(spinnerGroups.getSelectedItem().toString());
            }
        });
    }

    /**
     * Выводит полный список деталей
     */
    private void getDetailList() {
        db.collection(helicopter.getNumber())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        details.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Detail item = document.toObject(Detail.class);
                            details.add(item);
                        }
                        showDetails(details);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Выводит список деталей выбранной группы
     * @param group - группа
     */
    private void getDetailListByGroup(String group) {
        db.collection(helicopter.getNumber())
            .whereEqualTo("group", group)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        details.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Detail item = document.toObject(Detail.class);
                            details.add(item);
                        }
                        showDetails(details);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Вывод списка деталей
     * @param details - массив деталей
     */
    private  void showDetails(ArrayList<Detail> details) {
        RecyclerView recyclerViewTime = findViewById(R.id.recycler_view_details);
        recyclerViewTime.setHasFixedSize(true);
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerAdapterDetails rList = new RecyclerAdapterDetails(details);
        recyclerViewTime.setAdapter(rList);
        rList.setOnDetailClick(this);
    }
}
