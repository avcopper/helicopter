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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Time;
import com.andrew.helicopter.Models.Work;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TimeActivity extends BaseActivity {
    LinearLayout messageMain;
    TextView messageText;
    Time time;
    Calendar calendar = Calendar.getInstance();

    TextView header;
    TextInputEditText dateContainer;
    TextInputEditText airContainer, earthContainer;
    Spinner airSpinner, earthSpinner;
    SwitchMaterial apply;
    TextInputEditText startContainer, selContainer, genContainer, commonContainer, landingContainer;
    Button timeSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        helicopter = (Helicopter) getIntent().getSerializableExtra("HELICOPTER");
        time = (Time) getIntent().getSerializableExtra("TIME");

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
            messageText.setText("?????????? ?????????????????? ???? ??????????????????.\n???????????????? ???????? ???? ?????????????? ????????????");
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
     * ?????????????????????????? ??????????????????
     */
    private void init() {
        header = findViewById(R.id.header);
        apply = findViewById(R.id.apply);
        dateContainer = findViewById(R.id.date);
        airContainer = findViewById(R.id.air);
        airSpinner = findViewById(R.id.air_spinner);
        earthContainer = findViewById(R.id.earth);
        earthSpinner = findViewById(R.id.earth_spinner);
        startContainer = findViewById(R.id.start);
        selContainer = findViewById(R.id.sel);
        genContainer = findViewById(R.id.gen);
        commonContainer = findViewById(R.id.common);
        landingContainer = findViewById(R.id.landing);
        timeSave = findViewById(R.id.time_save);
    }

    /**
     * ?????????????????? ???????????????????????????? ???????????? ?? ????????
     */
    private void setText() {
        if (time != null) {
            SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                    getResources().getStringArray(R.array.math));
            header.setText(time.getDate());
            dateContainer.setText(time.getDate());
            airContainer.setText(DataHandler.simpleFormatDateFromMinutes(time.getAir()));
            airSpinner.setAdapter(spinnerAdapter);
            earthContainer.setText(DataHandler.simpleFormatDateFromMinutes(time.getEarth()));
            earthSpinner.setAdapter(spinnerAdapter);
            startContainer.setText(String.valueOf(time.getStart()));
            selContainer.setText(String.valueOf(time.getSel()));
            genContainer.setText(DataHandler.simpleFormatDateFromMinutes(time.getGen()));
            commonContainer.setText(DataHandler.simpleFormatDateFromMinutes(time.getCommon()));
            landingContainer.setText(String.valueOf(time.getLand()));
        }
    }

    /**
     * ?????????????????????????? ????????????????????????
     */
    private void setListeners() {
        // ???????????????????? ????????????
        timeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean applyData = apply.isChecked();
                String date = dateContainer.getText().toString();
                String air = airContainer.getText().toString();
                String airSign = airSpinner.getSelectedItem().toString();
                String earth = earthContainer.getText().toString();
                String earthSign = earthSpinner.getSelectedItem().toString();
                String start = startContainer.getText().toString();
                String sel = selContainer.getText().toString();
                String gen = genContainer.getText().toString();
                String common = commonContainer.getText().toString();
                String land = landingContainer.getText().toString();

                String[] timeArr = {air, earth, gen, common};
                String[] numArr = {start, sel, land};

                if (DataHandler.isValidTime(timeArr) && DataHandler.isValidDate(date) && DataHandler.isValidNumber(numArr)) {
                    int air_value = DataHandler.getMinutesFromTimeFormat(air);
                    int earth_value = DataHandler.getMinutesFromTimeFormat(earth);
                    Time tm = new Time(
                            date,
                            air_value,
                            earth_value,
                            Integer.parseInt(start),
                            Integer.parseInt(sel),
                            DataHandler.getMinutesFromTimeFormat(gen),
                            DataHandler.getMinutesFromTimeFormat(common),
                            Integer.parseInt(land)
                    );
                    Firebase fb = new Firebase("times/" + helicopter.getNumber() + "/" + getMonth(date), tm.getDate());
                    showAlertDialog(fb, tm, airSign, earthSign, applyData);
                } else {
                    Snackbar.make(toolbar,"?????????????????? ?????????????????? ????????????", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        showHelperWindow();
        setDataHandlerListeners();
    }

    /**
     * ?????????? ?????????????????????????????? ????????
     */
    private void showHelperWindow() {
        // ?????????? ?????????????????? ?????? ?????????? ????????
        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        setDate(dateContainer);
                    }
                };
                DatePickerDialog dateDialog = new DatePickerDialog(TimeActivity.this, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ??????????????
        airContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(airContainer);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(TimeActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ??????????????
        earthContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(earthContainer);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(TimeActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ??????????????
        genContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(genContainer);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(TimeActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ??????????????
        commonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(commonContainer);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(TimeActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
    }

    /**
     * ???????????????? ?????????????????? ????????????
     */
    private void setDataHandlerListeners() {
        // ?????????????????? ???????? ?? ?????????????????? ?????? ?????????????????? ???? ?? ???????? ?????????? ?? ???????????????? ???????????? ?? ????????
        dateContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidDate(userData)) {
                    dateContainer.setError(null);
                    header.setText(userData);
                } else {
                    dateContainer.setError("?????????????????? ?????????????????? ????????????");
                }
            }
        });
        // ???????????????? ???????????? ?? ???????? ????????????
        airContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) airContainer.setError(null);
                else airContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ?? ???????? ??????????
        earthContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) earthContainer.setError(null);
                else earthContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ?? ???????? ????????????
        startContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) startContainer.setError(null);
                else startContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ?? ???????? ??????????
        selContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) selContainer.setError(null);
                else selContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ?? ???????? ??????. ??????????
        genContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) genContainer.setError(null);
                else genContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ?? ???????? ?????????? ??????????
        commonContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) commonContainer.setError(null);
                else commonContainer.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????? ?????? ?????????????????????????? ???????????????????? ????????????
     * @param fb - ???????????? ????
     * @param tm - ??????????
     * @param airSign - ???????? ???????????????? ?????????????? ?????????????? (??????????????????????/??????????????????)
     * @param earthSign - ???????? ???????????????? ?????????????? ?????????? (??????????????????????/??????????????????)
     * @param applyData - ?????????????????? ?????????? ?? ?????????????? ???????? ??????????????/??????????
     */
    private void showAlertDialog(Firebase fb, Time tm, String airSign, String earthSign, boolean applyData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("???? ???????????????");
        builder.setMessage("???? ??????????????, ?????? ???????????? ?????????????????? ?????????????");
        builder.setPositiveButton("????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fb.saveDocument(tm);

                if (applyData) {
                    setTimeToDetails(tm, airSign, earthSign);
                    setTimeToWorks(tm, airSign, earthSign);
                } else {
                    Snackbar.make(toolbar ,"???????????? ??????????????????", Snackbar.LENGTH_INDEFINITE)
                        .setAction("????" , new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();;
                            }
                        }).show();
                }
            }
        });
        builder.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * ????????????????????/?????????????????? ?????????????? ?? ?????????????? ??????????????
     * @param tm - ??????????
     * @param airSign - ???????? ???????????????? ?????????????? ?????????????? (??????????????????????/??????????????????)
     * @param earthSign - ???????? ???????????????? ?????????????? ?????????? (??????????????????????/??????????????????)
     */
    private void setTimeToDetails(Time tm, String airSign, String earthSign) {

        db.collection(helicopter.getNumber())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int time = 0;
                        time += (Objects.equals(airSign, "-") ? (tm.getAir() * -1) : tm.getAir());
                        time += ((Objects.equals(earthSign, "-") ? (tm.getEarth() * -1) : tm.getEarth()) / 5);

                        int start = tm.getStart();
                        int sel = tm.getSel();
                        int gen = tm.getGen();
                        int common = tm.getCommon();
                        int land = tm.getLand();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Detail item = document.toObject(Detail.class);

                            if (time != 0) {
                                if (item.getResourceGlobal() != 0) {
                                    item.setResourceGlobalCurrent(item.getResourceGlobalCurrent() + time);
                                    item.setResourceGlobalBalance(item.getResourceGlobal() - item.getResourceGlobalCurrent());
                                }

                                if (item.getResourceRepair() != 0) {
                                    item.setResourceRepairCurrent(item.getResourceRepairCurrent() + time);
                                    item.setResourceRepairBalance(item.getResourceRepair() - item.getResourceRepairCurrent());
                                }
                            }

                            if (start != 0) {
                                if (item.getStartGlobal() != 0) {
                                    item.setStartGlobalCurrent(item.getStartGlobalCurrent() + start);
                                    item.setStartGlobalBalance(item.getStartGlobal() - item.getStartGlobalCurrent());
                                }

                                if (item.getStartRepair() != 0) {
                                    item.setStartRepairCurrent(item.getStartRepairCurrent() + start);
                                    item.setStartRepairBalance(item.getStartRepair() - item.getStartRepairCurrent());
                                }
                            }

                            if (sel != 0) {
                                if (item.getSelGlobal() != 0) {
                                    item.setSelGlobalCurrent(item.getSelGlobalCurrent() + sel);
                                    item.setSelGlobalBalance(item.getSelGlobal() - item.getSelGlobalCurrent());
                                }

                                if (item.getSelRepair() != 0) {
                                    item.setSelRepairCurrent(item.getSelRepairCurrent() + sel);
                                    item.setSelRepairBalance(item.getSelRepair() - item.getSelRepairCurrent());
                                }
                            }

                            if (gen != 0) {
                                if (item.getGenGlobal() != 0) {
                                    item.setGenGlobalCurrent(item.getGenGlobalCurrent() + gen);
                                    item.setGenGlobalBalance(item.getGenGlobal() - item.getGenGlobalCurrent());
                                }

                                if (item.getGenRepair() != 0) {
                                    item.setGenRepairCurrent(item.getGenRepairCurrent() + gen);
                                    item.setGenRepairBalance(item.getGenRepair() - item.getGenRepairCurrent());
                                }
                            }

                            if (common != 0) {
                                if (item.getCommonGlobal() != 0) {
                                    item.setCommonGlobalCurrent(item.getCommonGlobalCurrent() + common);
                                    item.setCommonGlobalBalance(item.getCommonGlobal() - item.getCommonGlobalCurrent());
                                }

                                if (item.getCommonRepair() != 0) {
                                    item.setCommonRepairCurrent(item.getCommonRepairCurrent() + common);
                                    item.setCommonRepairBalance(item.getCommonRepair() - item.getCommonRepairCurrent());
                                }
                            }

                            if (land != 0) {
                                if (item.getLandGlobal() != 0) {
                                    item.setLandGlobalCurrent(item.getLandGlobalCurrent() + land);
                                    item.setLandGlobalBalance(item.getLandGlobal() - item.getLandGlobalCurrent());
                                }

                                if (item.getLandRepair() != 0) {
                                    item.setLandRepairCurrent(item.getLandRepairCurrent() + land);
                                    item.setLandRepairBalance(item.getLandRepair() - item.getLandRepairCurrent());
                                }
                            }

                            Firebase fb = new Firebase(helicopter.getNumber(), item.getId());
                            fb.saveDocument(item);
                        }
                    } else {
                        Snackbar.make(toolbar , "???? ?????????????? ???????????????? ???????????? ??????????????: " + task.getException(), Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });
    }

    /**
     * ????????????????????/?????????????????? ?????????????? ?? ?????????????? ??????????
     * @param tm - ??????????
     * @param airSign - ???????? ???????????????? ?????????????? ?????????????? (??????????????????????/??????????????????)
     * @param earthSign - ???????? ???????????????? ?????????????? ?????????? (??????????????????????/??????????????????)
     */
    private void setTimeToWorks(Time tm, String airSign, String earthSign) {
        db.collection("works" + helicopter.getNumber())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int time = 0;
                        time += (Objects.equals(airSign, "-") ? (tm.getAir() * -1) : tm.getAir());
                        time += ((Objects.equals(earthSign, "-") ? (tm.getEarth() * -1) : tm.getEarth()) / 5);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Work item = document.toObject(Work.class);

                            if (time != 0 && item.getResourceHour() != 0) {
                                item.setResourceHourCurrent(item.getResourceHourCurrent() + time);
                                item.setResourceHourBalance(item.getResourceHour() - item.getResourceHourCurrent());

                                Firebase fb = new Firebase("works" + helicopter.getNumber(), item.getName());
                                fb.saveDocument(item);
                            }
                        }

                        Snackbar.make(toolbar ,"???????????? ??????????????????", Snackbar.LENGTH_INDEFINITE)
                            .setAction("????" , new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();;
                                }
                            }).show();
                    } else {
                        Snackbar.make(toolbar ,"???? ?????????????? ???????????????? ???????????? ??????????: " + task.getException(), Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });
    }

    /**
     * ???????????????????? ???????? ?? ?????????????? ??????????.?????? (MM.yyyy) ???? ???????? ?? ?????????????? ????????.??????????.?????? (dd.MM.yyyy)
     * @param date - ????????
     * @return
     */
    String getMonth(String date) {
        DateFormat oldFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        DateFormat newFormat = new SimpleDateFormat("MM.yyyy", Locale.ENGLISH);
        Date tempDate = null;
        try {
            tempDate = oldFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newFormat.format(tempDate);
    }

    /**
     * ?????????????????? ???????? ?? ????????
     * @param container - ?????????????????? ?????? ????????
     */
    private void setDate(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
    }

    /**
     * ?????????????????? ?????????????? ?? ????????
     * @param container - ?????????????????? ?????? ??????????????
     */
    private void setTime(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
    }
}
