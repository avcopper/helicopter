package com.andrew.helicopter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Work;
import com.andrew.helicopter.Recycler.RecyclerAdapterWorks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class WorkListActivity extends BaseActivity implements RecyclerAdapterWorks.OnItemWorkClicked {
    private static final String TABLE = "works";
    LinearLayout messageMain;
    TextView messageText;
    Toolbar toolbar;
    ArrayList<Work> works = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);

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

            getWorkList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.work, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload: {
                getWorkList();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Клик по выбранном пункту работ
     * @param position - позиция выбранного пункта в массиве работ
     */
    @Override
    public void onItemWorkClick(int position) {
        if (helicopter != null && works != null) {
            Work work = works.get(position);
            Intent intent = new Intent(this, WorkActivity.class);
            intent.putExtra("HELICOPTER", helicopter) ;
            intent.putExtra("WORK", work);
            intent.putExtra("CURRENT_USER", currentUser);
            startActivity(intent);
        }
    }

    /**
     * Возвращает полный список работ для данного вертолета
     */
    private void getWorkList() {
        db.collection(TABLE + helicopter.getNumber())
            .orderBy("sort")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        works.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Work item = document.toObject(Work.class);
                            works.add(item);
                        }
                        showWorks(works);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Вывод списка работ
     * @param works - массив работ
     */
    private  void showWorks(ArrayList<Work> works) {
        RecyclerView recyclerViewWork = findViewById(R.id.recycler_view_works);
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerAdapterWorks rList = new RecyclerAdapterWorks(works);
        recyclerViewWork.setAdapter(rList);
        rList.setOnWorkClick(this);
    }
}
