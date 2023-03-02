package com.andrew.helicopter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.User;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TABLE = "users";
    public static final String APP_PREFERENCES = "appsettings";
    public static final String APP_USER_NAME = "user_name";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences;
    RelativeLayout root;
    TextInputEditText loginField, passwordField;
    Button[] buttonNum = new Button[10];
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        db.collection("165")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Log.d("FB - TASK", String.valueOf(task.isSuccessful()));
//
//                        if (task.isSuccessful()) {
//                            Firebase fb = new Firebase("template");
//                            int i = 1;
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Detail item = document.toObject(Detail.class);
//                                Log.d("FB - " + i, item.toString());
//
//                                fb.setDocument(item.getId());
//                                fb.saveDocument(item);
//
//                                i++;
//                            }
//                        }
//                    }
//                });

        init();
        setListeners();
    }

    /**
     * Инициализация элементов
     */
    private void init() {
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        root = findViewById(R.id.root_layout);
        loginField = findViewById(R.id.login);
        passwordField = findViewById(R.id.password);
        back = findViewById(R.id.back);
        // ввод ранее использовавшегося имени пользователя
        if(sharedPreferences.contains(APP_USER_NAME)) {
            loginField.setText(sharedPreferences.getString(APP_USER_NAME, ""));
        }
        // инициализация кнопок цифровой клавиатуры
        for (int i = 0; i < buttonNum.length; i++){
            int recourceId= getResources().getIdentifier("num" + i, "id", getPackageName());
            buttonNum[i] = (Button) findViewById(recourceId);
            int finalI = i;
            buttonNum[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentPassword = passwordField.getText().toString();
                    String pressedNum = buttonNum[finalI].getText().toString();
                    passwordField.setText(currentPassword + pressedNum);
                }
            });
        }
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // проверка введенного пароля
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String userData = editable.toString();
                if (DataHandler.isValidNumber(userData) && userData.length() > 5) {
                    passwordField.setError(null);
                    authorize();
                }
                else passwordField.setError("Проверьте введенные данные");
            }
        });
        // удаление последнего введенного символа
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = passwordField.getText().toString();
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                    passwordField.setText(text);
                }
            }
        });
    }

    /**
     * Авторизация
     */
    private void authorize() {
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();

        if (login.isEmpty()) {
            loginField.setError("Введите логин");
            passwordField.setError(null);
            Toast.makeText(LoginActivity.this, "Введите логин", Toast.LENGTH_LONG).show();
        }
        else if(!DataHandler.isValidNumber(password) || password.length() < 6) {
            passwordField.setError("Введите пароль");
            loginField.setError(null);
            Toast.makeText(LoginActivity.this, "Введите пароль", Toast.LENGTH_LONG).show();
        }
        else {
            loginField.setError(null);
            passwordField.setError(null);

            db.collection(TABLE).document(login).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (!document.exists()) {
                            loginField.setError("Неверный логин или пароль");
                            passwordField.setError("Неверный логин или пароль");
                            Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                        } else {
                            loginField.setError(null);
                            passwordField.setError(null);
                            User item = document.toObject(User.class);

                            if (!Objects.equals(item.getPassword(), password)) {
                                loginField.setError("Неверный логин или пароль");
                                passwordField.setError("Неверный логин или пароль");
                                Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                            } else {
                                loginField.setError(null);
                                passwordField.setError(null);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(APP_USER_NAME, login);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("CURRENT_USER", item);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
}
