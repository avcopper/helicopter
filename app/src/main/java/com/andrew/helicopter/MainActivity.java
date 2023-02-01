package com.andrew.helicopter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Work;
import com.andrew.helicopter.Recycler.RecyclerAdapterDetails;
import com.andrew.helicopter.Recycler.RecyclerAdapterList;
import com.andrew.helicopter.Recycler.RecyclerAdapterWorks;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements RecyclerAdapterList.OnItemClicked,RecyclerAdapterList.OnItemLongClicked,
        RecyclerAdapterWorks.OnItemWorkClicked, RecyclerAdapterDetails.OnItemDetailClicked {
    ArrayList<Helicopter> helicopters = new ArrayList<>();
    ArrayList<Work> works = new ArrayList<>();
    ArrayList<Detail> details = new ArrayList<>();
    RelativeLayout recyclerMain;
    LinearLayout recyclerWorks;
    LinearLayout recyclerDetails;
    FloatingActionButton addHelicopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.title));
            actionBar.setDisplayShowTitleEnabled(true);
        }

        init();
        setListeners();

        if (helicopter == null || helicopter.getNumber() == null) {
            recyclerMain.setVisibility(View.VISIBLE);
            getHelicopters();
        } else {
            recyclerMain.setVisibility(View.INVISIBLE);
            getWorkList();
            getDetailList();
        }
    }

    /**
     * Обработка клика по выбранному вертолету
     * @param position - позиция выбранного пункта в массиве вертолетов
     */
    @Override
    public void onItemClick(int position) {
        helicopter.setNumber(helicopters.get(position).getNumber());
        setTitle(actionBar, helicopter.getNumber());
        recyclerMain.setVisibility(View.INVISIBLE);
        getWorkList();
        getDetailList();
    }

    @Override
    public void onItemLongClick(int position) {
        if (currentUser != null && currentUser.isAdmin()) {
            deleteHelicopter(helicopters.get(position).getNumber());
        }
    }

    /**
     * Обработка клика по выбранной работе
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
     * Обработка клика по выбранной детали
     * @param position - позиция выбранного пункта в массиве деталей
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
     * Обработка клика по пунктам главного меню
     * @param item - выбранный пункт
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select: {
                recyclerMain.setVisibility(View.VISIBLE);
                getHelicopters();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Инициализация элементов
     */
    private void init() {
        recyclerMain = findViewById(R.id.recycler_main);
        recyclerWorks = findViewById(R.id.recycler_work);
        recyclerDetails = findViewById(R.id.recycler_detail);
        addHelicopter = findViewById(R.id.add_helicopter);
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // добавление нового вертолета
        addHelicopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null && currentUser.isAdmin()) {
                    addHelicopter();
                }
            }
        });
    }

    /**
     * Выбор вертолета при запуске
     * @param helicopters - массив доступных вертолетов
     */
    protected void showSelectionDialog(ArrayList<Helicopter> helicopters) {
        RecyclerView recyclerViewTime = findViewById(R.id.recycler_view_helicopters);
        recyclerViewTime.setHasFixedSize(true);
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerAdapterList rList = new RecyclerAdapterList(helicopters);
        recyclerViewTime.setAdapter(rList);
        rList.setOnClick(this);
        rList.setOnLongClick(this);
    }

    /**
     * Получение списка доступных вертолетов из базы
     */
    public void getHelicopters() {
        helicopters.clear();
        db.collection("helicopters")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Helicopter item = document.toObject(Helicopter.class);
                            helicopters.add(item);
                            showSelectionDialog(helicopters);
                        }
                    } else {
                        recyclerMain.setVisibility(View.INVISIBLE);
                        Snackbar.make(toolbar ,"Не удалось получить данные. Проверьте наличие интренета", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Закрыть" , new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                    }
                }
            });
    }

    /**
     * Получение списка приближающихся работ
     */
    private void getWorkList() {
        works.clear();
        db.collection("works" + helicopter.getNumber())
            .orderBy("sort")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Work item = document.toObject(Work.class);

                            if (item.getResourceHour() > 0 && item.getResourceHour() <=1500 &&
                                    item.getResourceHourBalance() <= Work.LIMIT_SHORT) works.add(item);

                            if (item.getResourceHour() > 1500 &&
                                    item.getResourceHourBalance() <= Work.LIMIT_LONG) works.add(item);

                            if (item.getResourceMonth() > 0 &&
                                    item.getWorkDateNext() <= (DataHandler.getCurrentUnixTimestamp() + Work.LIMIT_MONTH)) works.add(item);
                        }
                        showWorks(works);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Получение списка деталей для ближайшего обслуживания
     */
    private void getDetailList() {
        details.clear();
        db.collection(helicopter.getNumber())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Detail item = document.toObject(Detail.class);

                            if (item.getResourceGlobal() > 0 && item.getResourceGlobalBalance() <= Detail.LIMIT_HOURS)
                                details.add(item);

                            if (item.getResourceRepair() > 0 && item.getResourceRepairBalance() <= Detail.LIMIT_HOURS)
                                details.add(item);

                            if (item.getResourceGlobalPeriod() > 0 &&
                                    item.getResourceGlobalNext() <= (DataHandler.getCurrentUnixTimestamp() + Detail.LIMIT_MONTHS))
                                details.add(item);

                            if (item.getResourceRepairPeriod() > 0 &&
                                    item.getResourceRepairNext() <= (DataHandler.getCurrentUnixTimestamp() + Detail.LIMIT_MONTHS))
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
     * Показ списка приближающихся работ
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

    /**
     * Показ списка деталей для ближайшего обслуживания
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

    /**
     * Добавление нового вертолета и всех его зависимых таблиц
     */
    private void addHelicopter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ведите номер");

        final TextInputLayout inputLayout = new TextInputLayout(MainActivity.this);
        final TextInputEditText input = new TextInputEditText(MainActivity.this);
        inputLayout.addView(input);
        inputLayout.setErrorEnabled(true);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setGravity(Gravity.CENTER);
        inputLayout.setPadding(30, 30, 30, 30);
        input.setTextSize(30);
        builder.setView(inputLayout);
        // проверка введенных данных
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) input.setError(null);
                else input.setError("Проверьте введенные данные");
            }
        });
        // сохранение данных в БД - создание вертолета, списка его деталей и работ
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = input.getText().toString();
                if (Integer.parseInt(number) > 0) {
                    addHelicopterToList(number);
                    addDetails(number);
                    addWorks(number);

                    try {
                        TimeUnit.SECONDS.sleep(1);
                        getHelicopters();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // отмена
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Удаление вертолета и всех его зависимых таблиц
     * @param number - номер вертолета
     */
    private void deleteHelicopter(String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ведите номер");
        builder.setMessage("Вы уверены, что хотите удалить вертолет " + number + "?");
        // удаление данных из БД - удаление вертолета, списка его деталей и работ
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDetails(number);
                deleteWorks(number);
                deleteTimes(number);
                deleteHelicopterFromList(number);

                try {
                    TimeUnit.SECONDS.sleep(1);
                    Toast.makeText(MainActivity.this, "Вертолет " + number + " удален.", Toast.LENGTH_LONG).show();
                    getHelicopters();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        // отмена
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Добавление нового вертолета
     * @param number - номер вертолета
     */
    private void addHelicopterToList(String number) {
        Helicopter h = new Helicopter(number);
        Firebase fb = new Firebase("helicopters", number);
        fb.saveDocument(h);
    }

    /**
     * Удаление вертолета
     * @param number - номер вертолета
     */
    private void deleteHelicopterFromList(String number) {
        Firebase fb = new Firebase("helicopters", number);
        fb.deleteDocument();
    }

    /**
     * Добавление списка деталей новому вертолету
     * @param number - номер вертолета
     */
    private void addDetails(String number) {
        Firebase fb = new Firebase("template");
        fb.copyTemplateDetailCollection(number);
    }

    /**
     * Удаление списка деталей вертолета
     * @param number - номер вертолета
     */
    private void deleteDetails(String number) {
        Firebase fb = new Firebase(number);
        fb.deleteCollection();
    }

    /**
     * Добавление списка работ новому вертолету
     * @param number - номер вертолета
     */
    private void addWorks(String number) {
        Firebase fb = new Firebase("worksTemplate");
        fb.copyTemplateWorkCollection("works" + number);
    }

    /**
     * Удаление списка работ вертолета
     * @param number - номер вертолета
     */
    private void deleteWorks(String number) {
        Firebase fb = new Firebase("works" + number);
        fb.deleteCollection();
    }

    /**
     * Удаление списка проведенных работ вертолета
     * @param number - номер вертолета
     */
    private void deleteTimes(String number) {
        Firebase fb = new Firebase("times", number);
        fb.deleteDocument();
    }
}
