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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;

public class DetailActivity extends BaseActivity {
    LinearLayout messageMain, blockMain, blockVsu, blockLand;
    TextView messageText;
    Detail detail;
    Calendar calendar = Calendar.getInstance();

    SwitchMaterial adminEdit;
    TextView header;
    Spinner group;
    TextInputEditText name, number, typeTemplate;
    TextInputEditText resourceGlobal, resourceGlobalCurrent, resourceGlobalBalance;
    TextInputEditText resourceRepair, resourceRepairCurrent, resourceRepairBalance;
    TextInputEditText resourceGlobalDate, resourceGlobalPeriod, resourceGlobalNext;
    TextInputEditText resourceRepairDate, resourceRepairPeriod, resourceRepairNext;
    TextInputEditText startGlobal, startGlobalCurrent, startGlobalBalance;
    TextInputEditText startRepair, startRepairCurrent, startRepairBalance;
    TextInputEditText selGlobal, selGlobalCurrent, selGlobalBalance;
    TextInputEditText selRepair, selRepairCurrent, selRepairBalance;
    TextInputEditText genGlobal, genGlobalCurrent, genGlobalBalance;
    TextInputEditText genRepair, genRepairCurrent, genRepairBalance;
    TextInputEditText commonGlobal, commonGlobalCurrent, commonGlobalBalance;
    TextInputEditText commonRepair, commonRepairCurrent, commonRepairBalance;
    TextInputEditText landGlobal, landGlobalCurrent, landGlobalBalance;
    TextInputEditText landRepair, landRepairCurrent, landRepairBalance;
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
            messageText.setText("?????????? ?????????????????? ???? ??????????????????.\n???????????????? ???????? ???? ?????????????? ????????????");
            messageMain.setVisibility(View.VISIBLE);
        } else {
            messageMain.setVisibility(View.INVISIBLE);
            setTitle(actionBar, helicopter.getNumber());

            init();
            setText();
            setVisibility();
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
        blockMain = findViewById(R.id.detail_block_main);
        blockVsu = findViewById(R.id.detail_block_vsu);
        blockLand = findViewById(R.id.detail_block_land);

        adminEdit = findViewById(R.id.admin_edit);
        header = findViewById(R.id.header);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        group = findViewById(R.id.group);
        group.setEnabled(false);
        typeTemplate = findViewById(R.id.type_template);

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

        startGlobal = findViewById(R.id.start_global);
        startGlobalCurrent = findViewById(R.id.start_global_current);
        startGlobalBalance = findViewById(R.id.start_global_balance);

        startRepair = findViewById(R.id.start_repair);
        startRepairCurrent = findViewById(R.id.start_repair_current);
        startRepairBalance = findViewById(R.id.start_repair_balance);

        selGlobal = findViewById(R.id.sel_global);
        selGlobalCurrent = findViewById(R.id.sel_global_current);
        selGlobalBalance = findViewById(R.id.sel_global_balance);

        selRepair = findViewById(R.id.sel_repair);
        selRepairCurrent = findViewById(R.id.sel_repair_current);
        selRepairBalance = findViewById(R.id.sel_repair_balance);

        genGlobal = findViewById(R.id.gen_global);
        genGlobalCurrent = findViewById(R.id.gen_global_current);
        genGlobalBalance = findViewById(R.id.gen_global_balance);

        genRepair = findViewById(R.id.gen_repair);
        genRepairCurrent = findViewById(R.id.gen_repair_current);
        genRepairBalance = findViewById(R.id.gen_repair_balance);

        commonGlobal = findViewById(R.id.common_global);
        commonGlobalCurrent = findViewById(R.id.common_global_current);
        commonGlobalBalance = findViewById(R.id.common_global_balance);

        commonRepair = findViewById(R.id.common_repair);
        commonRepairCurrent = findViewById(R.id.common_repair_current);
        commonRepairBalance = findViewById(R.id.common_repair_balance);

        landGlobal = findViewById(R.id.land_global);
        landGlobalCurrent = findViewById(R.id.land_global_current);
        landGlobalBalance = findViewById(R.id.land_global_balance);

        landRepair = findViewById(R.id.land_repair);
        landRepairCurrent = findViewById(R.id.land_repair_current);
        landRepairBalance = findViewById(R.id.land_repair_balance);

        detailSave = findViewById(R.id.detail_save);
    }

    /**
     * ?????????????????? ???????????????????????????? ???????????? ?? ????????
     */
    private void setText() {
        if (detail != null) {
            header.setText(detail.getId());
            name.setText(detail.getName());
            number.setText(detail.getNumber());
            group.setSelection(((ArrayAdapter)group.getAdapter()).getPosition(detail.getGroup()));
            typeTemplate.setText(String.valueOf(detail.getTypeTemplate()));

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

            startGlobal.setText(String.valueOf(detail.getStartGlobal()));
            startGlobalCurrent.setText(String.valueOf(detail.getStartGlobalCurrent()));
            startGlobalBalance.setText(String.valueOf(detail.getStartGlobalBalance()));

            startRepair.setText(String.valueOf(detail.getStartRepair()));
            startRepairCurrent.setText(String.valueOf(detail.getStartRepairCurrent()));
            startRepairBalance.setText(String.valueOf(detail.getStartRepairBalance()));

            selGlobal.setText(String.valueOf(detail.getSelGlobal()));
            selGlobalCurrent.setText(String.valueOf(detail.getSelGlobalCurrent()));
            selGlobalBalance.setText(String.valueOf(detail.getSelGlobalBalance()));

            selRepair.setText(String.valueOf(detail.getSelRepair()));
            selRepairCurrent.setText(String.valueOf(detail.getSelRepairCurrent()));
            selRepairBalance.setText(String.valueOf(detail.getSelRepairBalance()));

            genGlobal.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenGlobal()));
            genGlobalCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenGlobalCurrent()));
            genGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenGlobalBalance()));

            genRepair.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenRepair()));
            genRepairCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenRepairCurrent()));
            genRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getGenRepairBalance()));

            commonGlobal.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonGlobal()));
            commonGlobalCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonGlobalCurrent()));
            commonGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonGlobalBalance()));

            commonRepair.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonRepair()));
            commonRepairCurrent.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonRepairCurrent()));
            commonRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(detail.getCommonRepairBalance()));

            landGlobal.setText(String.valueOf(detail.getLandGlobal()));
            landGlobalCurrent.setText(String.valueOf(detail.getLandGlobalCurrent()));
            landGlobalBalance.setText(String.valueOf(detail.getLandGlobalBalance()));

            landRepair.setText(String.valueOf(detail.getLandRepair()));
            landRepairCurrent.setText(String.valueOf(detail.getLandRepairCurrent()));
            landRepairBalance.setText(String.valueOf(detail.getLandRepairBalance()));
        }
    }

    private void setVisibility() {
        if (!currentUser.isAdmin()) {
            adminEdit.setVisibility(View.INVISIBLE);
            adminEdit.setHeight(0);
        }

        switch (detail.getTypeTemplate()) {
            case 1: // ?????????????????????? ??????????????
                makeBlockInvisible(blockVsu);
                makeBlockInvisible(blockLand);
                break;
            case 2: // ??????
                makeBlockInvisible(blockMain);
                makeBlockInvisible(blockLand);
                break;
            case 3: // ??????????????
                makeBlockInvisible(blockMain);
                makeBlockInvisible(blockVsu);
                break;
            default:
                makeBlockInvisible(blockMain);
                makeBlockInvisible(blockVsu);
                makeBlockInvisible(blockLand);
        }
    }

    private void makeBlockInvisible(LinearLayout block) {
        block.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams paramsVsu = (LinearLayout.LayoutParams) block.getLayoutParams();
        paramsVsu.height = 0;
        block.setLayoutParams(paramsVsu);
    }

    /**
     * ?????????????????????????? ????????????????????????
     */
    private void setListeners() {
        showHelperWindow();
        setResourceTimeListeners();
        setResourceDateListeners();
        setStartListeners();
        setSelListeners();
        setGenListeners();
        setCommonListeners();
        setLandListeners();

        // ???????????????????????????? ?????????????? ?????????? ?????? ????????????
        adminEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEditable(isChecked);
            }
        });
        // ???????????????????? ????????????
        detailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typeTmpl = typeTemplate.getText().toString();
                Log.d("FB - typeTmpl", typeTmpl);

                String global = resourceGlobal.getText().toString();
                String globalCurrent = resourceGlobalCurrent.getText().toString();
                String repair = resourceRepair.getText().toString();
                String repairCurrent = resourceRepairCurrent.getText().toString();

                String globalDate = resourceGlobalDate.getText().toString();
                String globalPeriod = resourceGlobalPeriod.getText().toString();
                String repairDate = resourceRepairDate.getText().toString();
                String repairPeriod = resourceRepairPeriod.getText().toString();

                String startGlob = startGlobal.getText().toString();
                String startGlobCurrent = startGlobalCurrent.getText().toString();
                String startRep = startRepair.getText().toString();
                String startRepCurrent = startRepairCurrent.getText().toString();
                String selGlob = selGlobal.getText().toString();
                String selGlobCurrent = selGlobalCurrent.getText().toString();
                String selRep = selRepair.getText().toString();
                String selRepCurrent = selRepairCurrent.getText().toString();

                String genGlob = genGlobal.getText().toString();
                String genGlobCurrent = genGlobalCurrent.getText().toString();
                String genRep = genRepair.getText().toString();
                String genRepCurrent = genRepairCurrent.getText().toString();
                String commonGlob = commonGlobal.getText().toString();
                String commonGlobCurrent = commonGlobalCurrent.getText().toString();
                String commonRep = commonRepair.getText().toString();
                String commonRepCurrent = commonRepairCurrent.getText().toString();

                String landGlob = landGlobal.getText().toString();
                String landGlobCurrent = landGlobalCurrent.getText().toString();
                String landRep = landRepair.getText().toString();
                String landRepCurrent = landRepairCurrent.getText().toString();

                String[] dataTime = {
                    global, globalCurrent,repair, repairCurrent,
                    genGlob, genGlobCurrent, genRep, genRepCurrent,
                    commonGlob, commonGlobCurrent, commonRep, commonRepCurrent
                };
                String[] dataDate = {globalDate, repairDate};
                String[] dataNum = {
                        typeTmpl,
                        globalPeriod, repairPeriod,
                        startGlob, startGlobCurrent, startRep, startRepCurrent,
                        selGlob, selGlobCurrent, selRep, selRepCurrent,
                        landGlob, landGlobCurrent, landRep, landRepCurrent,
                };

                if (DataHandler.isValidTime(dataTime) && DataHandler.isValidDate(dataDate) && DataHandler.isValidNumber(dataNum)) {
                    Detail dtl = new Detail(
                        number.getText().toString(),
                        name.getText().toString(),
                        group.getSelectedItem().toString(),
                        header.getText().toString(),
                        Integer.parseInt(typeTmpl),
                        DataHandler.getMinutesFromTimeFormat(global),
                        DataHandler.getMinutesFromTimeFormat(globalCurrent),
                        DataHandler.getMinutesFromTimeFormat(repair),
                        DataHandler.getMinutesFromTimeFormat(repairCurrent),
                        DataHandler.getUnixTimestampFromDate(globalDate),
                        DataHandler.getUnixTimestampFromYears(Long.parseLong(globalPeriod)),
                        DataHandler.getUnixTimestampFromDate(repairDate),
                        DataHandler.getUnixTimestampFromYears(Long.parseLong(repairPeriod)),
                        Integer.parseInt(startGlob),
                        Integer.parseInt(startGlobCurrent),
                        Integer.parseInt(startRep),
                        Integer.parseInt(startRepCurrent),
                        Integer.parseInt(selGlob),
                        Integer.parseInt(selGlobCurrent),
                        Integer.parseInt(selRep),
                        Integer.parseInt(selRepCurrent),
                        DataHandler.getMinutesFromTimeFormat(genGlob),
                        DataHandler.getMinutesFromTimeFormat(genGlobCurrent),
                        DataHandler.getMinutesFromTimeFormat(genRep),
                        DataHandler.getMinutesFromTimeFormat(genRepCurrent),
                        DataHandler.getMinutesFromTimeFormat(commonGlob),
                        DataHandler.getMinutesFromTimeFormat(commonGlobCurrent),
                        DataHandler.getMinutesFromTimeFormat(commonRep),
                        DataHandler.getMinutesFromTimeFormat(commonRepCurrent),
                        Integer.parseInt(landGlob),
                        Integer.parseInt(landGlobCurrent),
                        Integer.parseInt(landRep),
                        Integer.parseInt(landRepCurrent)
                    );

                    Firebase fb = new Firebase(helicopter.getNumber(), header.getText().toString());
                    showAlertDialog(fb, dtl);
                } else {
                    Toast.makeText(DetailActivity.this, "?????????????????? ?????????????????? ????????????", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * ?????????? ???????????????????????????? ???????? ?????? ?????????? ????????, ??????????????...
     */
    private void showHelperWindow() {
        // ?????????? ?????????? ?????? ?????????? ?????????????? - ???????????? ??????????????????????
        resourceGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(resourceGlobal);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(DetailActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ?????????????? - ???????????? ?????????????????????? ??????????????
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
        // ?????????? ?????????? ?????? ?????????? ?????????????? - ???????????? ????????????????????????
        resourceRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        setTime(resourceRepair);
                    }
                };
                TimePickerDialog timeDialog = new TimePickerDialog(DetailActivity.this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timeDialog.show();
            }
        });
        // ?????????? ?????????? ?????? ?????????? ?????????????? - ???????????? ???????????????????????? ??????????????
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
        // ?????????? ?????????????????? ?????? ?????????? ???????? ??????????????
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
        // ?????????? ?????????????????? ?????? ?????????? ???????? ??????????????
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
    }

    /**
     * ???????????????? ?????????? ?????????? ??????????????
     */
    private void setResourceTimeListeners() {
        // ???????????????? ???????????????????????? ?????????????? ?????????????? ????????????
        resourceGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    resourceGlobal.setError(null);
                    int global = DataHandler.getMinutesFromTimeFormat(userData);
                    int current = detail.getResourceGlobalCurrent();
                    resourceGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(global - current));
                } else resourceGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????????????????? ?????????????? ?????????????? ????????????
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
                } else resourceGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????????????? ?????????????? ?????????????? ????????????
        resourceRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    resourceRepair.setError(null);
                    int repair = DataHandler.getMinutesFromTimeFormat(userData);
                    int current = detail.getResourceRepairCurrent();
                    resourceRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(repair - current));
                } else resourceRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????????????? ?????????????? ?????????????? ????????????
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
                } else resourceRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????????? ?????????? ?????????? ??????
     */
    private void setResourceDateListeners() {
        // ???????????????? ???????? ????????????
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
                } else resourceGlobalDate.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????? ????????????
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
                } else resourceGlobalPeriod.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????? ??????????????
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
                } else resourceRepairDate.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????? ??????????????
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
                } else resourceRepairPeriod.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????????? ?????????? ?????????? ??????????????
     */
    private void setStartListeners() {
        // ???????????????? ?????????????? ????????????????????????
        startGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    startGlobal.setError(null);
                    int startGlob = Integer.parseInt(userData);
                    int startGlobCurrent = Integer.parseInt(startGlobalCurrent.getText().toString());
                    startGlobalBalance.setText(String.valueOf(startGlob - startGlobCurrent));
                } else startGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ?????????????? ????????????????????????
        startGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    startGlobalCurrent.setError(null);
                    int startGlob = Integer.parseInt(startGlobal.getText().toString());
                    int startGlobCurrent = Integer.parseInt(userData);
                    startGlobalBalance.setText(String.valueOf(startGlob - startGlobCurrent));
                } else startGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ?????????????? ??????????????????????????
        startRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    startRepair.setError(null);
                    int startRep = Integer.parseInt(userData);
                    int startRepCurrent = Integer.parseInt(startRepairCurrent.getText().toString());
                    startRepairBalance.setText(String.valueOf(startRep - startRepCurrent));
                } else startRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ?????????????? ??????????????????????????
        startRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    startRepairCurrent.setError(null);
                    int startRep = Integer.parseInt(startRepair.getText().toString());
                    int startRepCurrent = Integer.parseInt(userData);
                    startRepairBalance.setText(String.valueOf(startRep - startRepCurrent));
                } else startRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????????? ?????????? ?????????? ??????????????
     */
    private void setSelListeners() {
        // ???????????????? ???????????? ????????????????????????
        selGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    selGlobal.setError(null);
                    int selGlob = Integer.parseInt(userData);
                    int selGlobCurrent = Integer.parseInt(selGlobalCurrent.getText().toString());
                    selGlobalBalance.setText(String.valueOf(selGlob - selGlobCurrent));
                } else selGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ????????????????????????
        selGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    selGlobalCurrent.setError(null);
                    int selGlob = Integer.parseInt(selGlobal.getText().toString());
                    int selGlobCurrent = Integer.parseInt(userData);
                    selGlobalBalance.setText(String.valueOf(selGlob - selGlobCurrent));
                } else selGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ??????????????????????????
        selRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    selRepair.setError(null);
                    int selRep = Integer.parseInt(userData);
                    int selRepCurrent = Integer.parseInt(selRepairCurrent.getText().toString());
                    selRepairBalance.setText(String.valueOf(selRep - selRepCurrent));
                } else selRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ??????????????????????????
        selRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    selRepairCurrent.setError(null);
                    int selRep = Integer.parseInt(selRepair.getText().toString());
                    int selRepCurrent = Integer.parseInt(userData);
                    selRepairBalance.setText(String.valueOf(selRep - selRepCurrent));
                } else selRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????????? ?????????? ?????????? ?????????????? ??????. ????????????
     */
    private void setGenListeners() {
        // ???????????????? ??????. ???????????? ????????????????????????
        genGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    genGlobal.setError(null);
                    int genGlob = DataHandler.getMinutesFromTimeFormat(userData);
                    int genGlobCurrent = DataHandler.getMinutesFromTimeFormat(genGlobalCurrent.getText().toString());
                    genGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(genGlob - genGlobCurrent));
                } else genGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ??????. ???????????? ????????????????????????
        genGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    genGlobalCurrent.setError(null);
                    int genGlob = DataHandler.getMinutesFromTimeFormat(genGlobal.getText().toString());
                    int genGlobCurrent = DataHandler.getMinutesFromTimeFormat(userData);
                    genGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(genGlob - genGlobCurrent));
                } else genGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ??????. ???????????? ??????????????????????????
        genRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    genRepair.setError(null);
                    int genRep = DataHandler.getMinutesFromTimeFormat(userData);
                    int genRepCurrent = DataHandler.getMinutesFromTimeFormat(genRepairCurrent.getText().toString());
                    genRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(genRep - genRepCurrent));
                } else genRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ??????. ???????????? ??????????????????????????
        genRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    genRepairCurrent.setError(null);
                    int genRep = DataHandler.getMinutesFromTimeFormat(genRepair.getText().toString());
                    int genRepCurrent = DataHandler.getMinutesFromTimeFormat(userData);
                    genRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(genRep - genRepCurrent));
                } else genRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ???????????????? ?????????? ?????????? ???????????? ??????????????
     */
    private void setCommonListeners() {
        // ???????????????? ???????????? ????????????????????????
        commonGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    commonGlobal.setError(null);
                    int commonGlob = DataHandler.getMinutesFromTimeFormat(userData);
                    int commonGlobCurrent = DataHandler.getMinutesFromTimeFormat(commonGlobalCurrent.getText().toString());
                    commonGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(commonGlob - commonGlobCurrent));
                } else commonGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ????????????????????????
        commonGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    commonGlobalCurrent.setError(null);
                    int commonGlob = DataHandler.getMinutesFromTimeFormat(commonGlobal.getText().toString());
                    int commonGlobCurrent = DataHandler.getMinutesFromTimeFormat(userData);
                    commonGlobalBalance.setText(DataHandler.simpleFormatDateFromMinutes(commonGlob - commonGlobCurrent));
                } else commonGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ??????????????????????????
        commonRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    commonRepair.setError(null);
                    int commonRep = DataHandler.getMinutesFromTimeFormat(userData);
                    int commonRepCurrent = DataHandler.getMinutesFromTimeFormat(commonRepairCurrent.getText().toString());
                    commonRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(commonRep - commonRepCurrent));
                } else commonRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????? ??????????????????????????
        commonRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidTime(userData)) {
                    commonRepairCurrent.setError(null);
                    int commonRep = DataHandler.getMinutesFromTimeFormat(commonRepair.getText().toString());
                    int commonRepCurrent = DataHandler.getMinutesFromTimeFormat(userData);
                    commonRepairBalance.setText(DataHandler.simpleFormatDateFromMinutes(commonRep - commonRepCurrent));
                } else commonRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    private void setLandListeners() {
        // ???????????????? ?????????? ??????????????
        landGlobal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    landGlobal.setError(null);
                    int landGlob = Integer.parseInt(userData);
                    int landGlobCurrent = Integer.parseInt(landGlobalCurrent.getText().toString());
                    landGlobalBalance.setText(String.valueOf(landGlob - landGlobCurrent));
                } else landGlobal.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ?????????? ??????????????
        landGlobalCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    landGlobalCurrent.setError(null);
                    int landGlob = Integer.parseInt(landGlobal.getText().toString());
                    int landGlobCurrent = Integer.parseInt(userData);
                    landGlobalBalance.setText(String.valueOf(landGlob - landGlobCurrent));
                } else landGlobalCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????????????????? ??????????????
        landRepair.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    landRepair.setError(null);
                    int landRep = Integer.parseInt(userData);
                    int landRepCurrent = Integer.parseInt(landRepairCurrent.getText().toString());
                    landRepairBalance.setText(String.valueOf(landRep - landRepCurrent));
                } else landRepair.setError("?????????????????? ?????????????????? ????????????");
            }
        });
        // ???????????????? ???????????????????????? ??????????????
        landRepairCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData)) {
                    landRepairCurrent.setError(null);
                    int landRep = Integer.parseInt(landRepair.getText().toString());
                    int landRepCurrent = Integer.parseInt(userData);
                    landRepairBalance.setText(String.valueOf(landRep - landRepCurrent));
                } else landRepairCurrent.setError("?????????????????? ?????????????????? ????????????");
            }
        });
    }

    /**
     * ????????????????/???????????????? ?????????????? ?????????? ?????? ???????????????????????????? ????????????
     * @param isEditable - ????????
     */
    private void setEditable(boolean isEditable) {
        name.setEnabled(isEditable);
        group.setEnabled(isEditable);
        group.setEnabled(isEditable);
        typeTemplate.setEnabled(isEditable);

        resourceGlobal.setEnabled(isEditable);
        resourceRepair.setEnabled(isEditable);
        startGlobal.setEnabled(isEditable);
        startRepair.setEnabled(isEditable);
        selGlobal.setEnabled(isEditable);
        selRepair.setEnabled(isEditable);
        genGlobal.setEnabled(isEditable);
        genRepair.setEnabled(isEditable);
        commonGlobal.setEnabled(isEditable);
        commonRepair.setEnabled(isEditable);
        landGlobal.setEnabled(isEditable);
        landRepair.setEnabled(isEditable);
    }

    /**
     * ???????????? ?????????????????????????? ???????????????????? ????????????
     * @param fb - ???????????? ????
     * @param dtl - ?????????????????????? ????????????
     */
    private void showAlertDialog(Firebase fb, Detail dtl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("???? ???????????????");
        builder.setMessage("???? ??????????????, ?????? ???????????? ?????????????????? ?????????????");
        builder.setPositiveButton("????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fb.saveDocument(dtl);
                finish();
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
     * ?????????????????? ?????????????? ?? ????????
     * @param container - ?????????????????? ?????? ??????????????
     */
    private void setTime(TextInputEditText container) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.ENGLISH);
        Date d = new Date(calendar.getTimeInMillis());
        container.setText(sdf.format(d));
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
}
