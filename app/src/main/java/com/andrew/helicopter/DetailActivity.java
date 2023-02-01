package com.andrew.helicopter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;

public class DetailActivity extends BaseActivity {
    LinearLayout messageMain;
    TextView messageText;
    Detail detail;
    Calendar calendar = Calendar.getInstance();

    TextView header;
    TextInputEditText name, number, group;
    TextInputEditText resourceGlobal, resourceGlobalCurrent, resourceGlobalBalance;
    TextInputEditText resourceRepair, resourceRepairCurrent, resourceRepairBalance;
    TextInputEditText resourceGlobalDate, resourceGlobalPeriod, resourceGlobalNext;
    TextInputEditText resourceRepairDate, resourceRepairPeriod, resourceRepairNext;
    Button detailSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        helicopter = (Helicopter) getIntent().getSerializableExtra("HELICOPTER");
        detail = (Detail) getIntent().getSerializableExtra("DETAIL");

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
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        group = findViewById(R.id.group);
        resourceGlobal = findViewById(R.id.resource_global);
        resourceGlobalCurrent = findViewById(R.id.resource_global_current);
        resourceGlobalBalance = findViewById(R.id.resource_global_balance);
        resourceRepair = findViewById(R.id.resource_repair);
        resourceRepairCurrent = findViewById(R.id.resource_repair_current);
        resourceRepairBalance = findViewById(R.id.resource_repair_balance);
        resourceGlobalDate = findViewById(R.id.resource_global_date);
        resourceGlobalPeriod = findViewById(R.id.resource_global_period);
        resourceGlobalNext = findViewById(R.id.resource_global_next);
        resourceRepairDate = findViewById(R.id.resource_repair_date);
        resourceRepairPeriod = findViewById(R.id.resource_repair_period);
        resourceRepairNext = findViewById(R.id.resource_repair_next);
        detailSave = findViewById(R.id.detail_save);
    }

    /**
     * Установка первоначальных данных в поля
     */
    private void setText() {
        if (detail != null) {
            header.setText(detail.getId());
            name.setText(detail.getName());
            number.setText(detail.getNumber());
            group.setText(detail.getGroup());

            resourceGlobal.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceGlobal()));
            resourceGlobalCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceGlobalCurrent()));
            resourceGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceGlobalBalance()));

            resourceRepair.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceRepair()));
            resourceRepairCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceRepairCurrent()));
            resourceRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getResourceRepairBalance()));

            resourceGlobalPeriod.setText(DataHandler.getYearsFromUnixTimestamp(detail.getResourceGlobalPeriod()));
            resourceGlobalDate.setText(DataHandler.getDateFromUnixTimestamp(detail.getResourceGlobalDate()));
            resourceGlobalNext.setText(DataHandler.getDateFromUnixTimestamp(detail.getResourceGlobalNext()));

            resourceRepairPeriod.setText(DataHandler.getYearsFromUnixTimestamp(detail.getResourceRepairPeriod()));
            resourceRepairDate.setText(DataHandler.getDateFromUnixTimestamp(detail.getResourceRepairDate()));
            resourceRepairNext.setText(DataHandler.getDateFromUnixTimestamp(detail.getResourceRepairNext()));
        }
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // сохранение данных
        detailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String global = resourceGlobal.getText().toString();
                String globalCurrent = resourceGlobalCurrent.getText().toString();
                String repair = resourceRepair.getText().toString();
                String repairCurrent = resourceRepairCurrent.getText().toString();

                String globalDate = resourceGlobalDate.getText().toString();
                String globalPeriod = resourceGlobalPeriod.getText().toString();
                String repairDate = resourceRepairDate.getText().toString();
                String repairPeriod = resourceRepairPeriod.getText().toString();

                String[] dataTime = {global, globalCurrent,repair, repairCurrent};
                String[] dataDate = {globalDate, repairDate};
                String[] dataNum = {globalPeriod, repairPeriod};
                if (DataHandler.isValidTime(dataTime) && DataHandler.isValidDate(dataDate) && DataHandler.isValidNumber(dataNum)) {
                    Detail dtl = new Detail(
                        number.getText().toString(),
                        name.getText().toString(),
                        group.getText().toString(),
                        header.getText().toString(),
                        DataHandler.getMinutesFromTimeFormat(global),
                        DataHandler.getMinutesFromTimeFormat(globalCurrent),
                        DataHandler.getMinutesFromTimeFormat(repair),
                        DataHandler.getMinutesFromTimeFormat(repairCurrent),
                        DataHandler.getUnixTimestampFromDate(globalDate),
                        DataHandler.getUnixTimestampFromYears(Long.parseLong(globalPeriod)),
                        DataHandler.getUnixTimestampFromDate(repairDate),
                        DataHandler.getUnixTimestampFromYears(Long.parseLong(repairPeriod))
                    );

                    Firebase fb = new Firebase(helicopter.getNumber(), header.getText().toString());
                    showAlertDialog(fb, dtl);
                } else {
                    Toast.makeText(DetailActivity.this, "Проверьте введенные данные", Toast.LENGTH_LONG).show();
                }
            }
        });
        // показ часов для ввода времени
        resourceGlobalCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(resourceGlobalCurrent);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(DetailActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // показ часов для ввода времени
        resourceRepairCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(resourceRepairCurrent);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(DetailActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // показ календаря для ввода даты выпуска
        resourceGlobalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        setDate(resourceGlobalDate);
                    }
                };
                DatePickerDialog dateDialog = new DatePickerDialog(DetailActivity.this, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
            }
        });
        // показ календаря для ввода даты ремонта
        resourceRepairDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        setDate(resourceRepairDate);
                    }
                };
                DatePickerDialog dateDialog = new DatePickerDialog(DetailActivity.this, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
            }
        });
        // пересчет назначенного остатка ресурса детали
        resourceGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    resourceGlobalCurrent.setError(null);
                    int current = DataHandler.getMinutesFromTimeFormat(userData);
                    int global = detail.getResourceGlobal();
                    resourceGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(global - current));
                } else resourceGlobalCurrent.setError("Проверьте введенные данные");
            }
        });
        // пересчет ремонтного остатка ресурса детали
        resourceRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    resourceRepairCurrent.setError(null);
                    int current = DataHandler.getMinutesFromTimeFormat(userData);
                    int repair = detail.getResourceRepair();
                    resourceRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(repair - current));
                } else resourceRepairCurrent.setError("Проверьте введенные данные");
            }
        });
        // пересчет даты замены
        resourceGlobalDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidDate(userData)) {
                    resourceGlobalDate.setError(null);
                    long globalDate = DataHandler.getUnixTimestampFromDate(userData);
                    long globalPeriod = detail.getResourceGlobalPeriod();
                    resourceGlobalNext.setText(DataHandler.getDateFromUnixTimestamp(globalDate + globalPeriod));
                } else resourceGlobalDate.setError("Проверьте введенные данные");
            }
        });
        // пересчет даты замены
        resourceGlobalPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();

                if (DataHandler.isValidNumber(userData)) {
                    resourceGlobalPeriod.setError(null);
                    long globalPeriod = DataHandler.getUnixTimestampFromYears(Double.parseDouble(userData));
                    long globalDate = detail.getResourceGlobalDate();
                    resourceGlobalNext.setText(DataHandler.getDateFromUnixTimestamp(globalPeriod + globalDate));
                } else resourceGlobalPeriod.setError("Проверьте введенные данные");
            }
        });
        // пересчет даты ремонта
        resourceRepairDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidDate(userData)) {
                    resourceRepairDate.setError(null);
                    long repairDate = DataHandler.getUnixTimestampFromDate(userData);
                    long repairPeriod = detail.getResourceRepairPeriod();
                    resourceRepairNext.setText(DataHandler.getDateFromUnixTimestamp(repairDate + repairPeriod));
                } else resourceRepairDate.setError("Проверьте введенные данные");
            }
        });
        // пересчет даты ремонта
        resourceRepairPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    resourceRepairPeriod.setError(null);
                    long repairPeriod = DataHandler.getUnixTimestampFromYears(Double.parseDouble(userData));
                    long repairDate = detail.getResourceRepairDate();
                    resourceRepairNext.setText(DataHandler.getDateFromUnixTimestamp(repairDate + repairPeriod));
                } else resourceRepairPeriod.setError("Проверьте введенные данные");
            }
        });
    }

    /**
     * Диалог подтверждения сохранения данных
     * @param fb - объект БД
     * @param dtl - сохраняемая деталь
     */
    private void showAlertDialog(Firebase fb, Detail dtl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы уверены?");
        builder.setMessage("Вы уверены, что хотите сохранить данные?");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fb.saveDocument(dtl);
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
     * Установка времени в поле
     * @param container - контейнер для времени
     */
    private void setTime(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
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
}
