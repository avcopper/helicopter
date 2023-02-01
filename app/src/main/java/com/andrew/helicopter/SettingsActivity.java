package com.andrew.helicopter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.andrew.helicopter.Models.User;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends BaseActivity {
    private static final String TABLE = "users";
    Toolbar toolbar;
    TextInputEditText loginField, roleField, emailField, passwordField;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.menu_settings));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        setData();
        setListeners();
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
        loginField = findViewById(R.id.login);
        roleField = findViewById(R.id.role);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        saveButton = findViewById(R.id.user_save);
    }

    /**
     * Установка данных в поля
     */
    private void setData() {
        if (currentUser != null) {
            loginField.setText(currentUser.getLogin());
            roleField.setText(currentUser.getRole());
            emailField.setText(currentUser.getEmail());
            passwordField.setText(currentUser.getPassword());
        }
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // проверка введенного email
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidEmail(userData)) emailField.setError(null);
                else emailField.setError("Проверьте введенные данные");
            }
        });
        // проверка введенного пароля
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData) && userData.length() > 4) passwordField.setError(null);
                else passwordField.setError("Проверьте введенные данные");
            }
        });
        // добавление нового пользователя
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog();
            }
        });
    }

    /**
     * Сохранение данных пользователя
     */
    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Вы уверены?");
        builder.setMessage("Вы уверены, что хотите сохранить данные?");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String login = loginField.getText().toString();
                String role = roleField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (DataHandler.isValidEmail(email) && DataHandler.isValidNumber(password)) {
                    User user = new User(login, email, password);
                    if (role.equals("admin")) user.setRole(role);

                    Firebase fb = new Firebase(TABLE, login);
                    fb.saveDocument(user);
                    finish();
                } else {
                    Toast.makeText(SettingsActivity.this, "Проверьте введенные данные", Toast.LENGTH_LONG).show();
                }
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
}
