package com.andrew.helicopter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Work;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkActivity extends BaseActivity {
    private static final String TABLE = "works";
    LinearLayout messageMain;
    TextView messageText;
    Work work;
    Calendar calendar = Calendar.getInstance();

    TextView header;
    TextInputEditText hourContainer, hourCurrentContainer, hourBalanceContainer;
    TextInputEditText monthContainer, workDateContainer, workNextContainer;
    TextView sortContainer;
    Button workSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        helicopter = (Helicopter) getIntent().getSerializableExtra("HELICOPTER");
        work = (Work) getIntent().getSerializableExtra("WORK");

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
            setText();
            setListeners();
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
     * Инициализация элементов
     */
    private void init() {
        header = findViewById(R.id.header);
        hourContainer = findViewById(R.id.hour);
        hourCurrentContainer = findViewById(R.id.hour_current);
        hourBalanceContainer = findViewById(R.id.hour_balance);
        monthContainer = findViewById(R.id.month);
        workDateContainer = findViewById(R.id.work_date);
        workNextContainer = findViewById(R.id.work_next);
        sortContainer = findViewById(R.id.sort);
        workSave = findViewById(R.id.time_save);
    }

    /**
     * Установка первоначальных данных в поля
     */
    private void setText() {
        if (work != null) {
            header.setText(work.getName());
            hourContainer.setText(DataHandler.simpleFormatDateFromMinutes(work.getResourceHour()));
            hourCurrentContainer.setText(DataHandler.simpleFormatDateFromMinutes(work.getResourceHourCurrent()));
            hourBalanceContainer.setText(DataHandler.simpleFormatDateFromMinutes(work.getResourceHourBalance()));
            monthContainer.setText(DataHandler.getMonthsFromUnixTimestamp(work.getResourceMonth()));
            workDateContainer.setText(DataHandler.getDateFromUnixTimestamp(work.getWorkDate()));
            workNextContainer.setText(DataHandler.getDateFromUnixTimestamp(work.getWorkDateNext()));
            sortContainer.setText(String.valueOf(work.getSort()));
        }
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // сохранение данных
        workSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = header.getText().toString();
                String hour = hourContainer.getText().toString();
                String hour_current = hourCurrentContainer.getText().toString();
                String month = monthContainer.getText().toString();
                String work = workDateContainer.getText().toString();
                String sort = sortContainer.getText().toString();
                String[] timeArr = {hour, hour_current};
                String[] numberArr = {month, sort};

                if (DataHandler.isValidTime(timeArr) && DataHandler.isValidDate(work) && DataHandler.isValidNumber(numberArr)) {
                    Work wk = new Work(
                            name,
                            DataHandler.getMinutesFromTimeFormat(hour),
                            DataHandler.getMinutesFromTimeFormat(hour_current),
                            DataHandler.getUnixTimestampFromMoths(Integer.parseInt(month)),
                            DataHandler.getUnixTimestampFromDate(work),
                            Integer.parseInt(sort)
                    );
                    Firebase fb = new Firebase(TABLE + helicopter.getNumber(), wk.getName());
                    showAlertDialog(fb, wk);
                } else {
                    Snackbar.make(toolbar,"Проверьте введенные данные", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        // показ календаря для ввода даты
        workDateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        setDate(workDateContainer);
                    }
                };
                DatePickerDialog dateDialog = new DatePickerDialog(WorkActivity.this, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
            }
        });
        // показ часов для ввода времени
        hourCurrentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(hourCurrentContainer);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(WorkActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // пересчет остатка часов до ремонта
        hourCurrentContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    hourCurrentContainer.setError(null);
                    int current = DataHandler.getMinutesFromTimeFormat(userData);
                    int hour = work.getResourceHour();
                    hourBalanceContainer.setText(DataHandler.simpleFormatDateFromMinutes(hour - current));
                } else hourCurrentContainer.setError("Проверьте введенные данные");
            }
        });
        // пересчет даты следующего ремонта
        workDateContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidDate(userData)) {
                    workDateContainer.setError(null);
                    long workDate = DataHandler.getUnixTimestampFromDate(userData);
                    long monthResource = work.getResourceMonth();
                    workNextContainer.setText(DataHandler.getDateFromUnixTimestamp(workDate + monthResource));
                } else workDateContainer.setError("Проверьте введенные данные");
            }
        });
    }

    /**
     * Диалог для подтверждения сохранения данных
     * @param fb - объект БД
     * @param tm - работа
     */
    private void showAlertDialog(Firebase fb, Work tm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы уверены?");
        builder.setMessage("Вы уверены, что хотите сохранить данные?");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fb.saveDocument(tm);
                finish();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Установка даты в поле
     * @param container - контейнер для даты
     */
    private void setDate(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
    }

    /**
     * Установка времени в поле
     * @param container - контейнер для времени
     */
    private void setTime(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
    }
}
