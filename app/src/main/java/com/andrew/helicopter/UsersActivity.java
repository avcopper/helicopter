package com.andrew.helicopter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andrew.helicopter.Models.User;
import com.andrew.helicopter.Recycler.RecyclerAdapterUsers;
import com.andrew.helicopter.System.DataHandler;
import com.andrew.helicopter.System.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Objects;

public class UsersActivity extends BaseActivity implements RecyclerAdapterUsers.OnUserClicked {
    private static final String TABLE = "users";
    FloatingActionButton addUser;
    ArrayList<User> users = new ArrayList<>();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.list_user));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        setListeners();
        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }

    /**
     * Обработка клика по пунктам главного меню
     * @param item - выбранный пункт
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserClick(int position) {
        if (users != null) {
            User user = users.get(position);
            showUserDialog(user);
        }
    }

    /**
     * Инициализация элементов
     */
    private void init() {
        addUser = findViewById(R.id.add_user);
        user = null;
    }

    /**
     * Инициализация обработчиков
     */
    private void setListeners() {
        // добавление нового пользователя
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDialog(user);
            }
        });
    }

    /**
     * Получение списка пользователей
     */
    private void getUsers() {
        db.collection(TABLE)
            .whereNotEqualTo("login", "andrew")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User item = document.toObject(User.class);
                            users.add(item);
                        }
                        showUsers(users);
                    } else {
                        Snackbar.make(toolbar , "Не удалось получить данные.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Показ списка пользователей
     * @param users - массив пользователей
     */
    private void showUsers(ArrayList<User> users) {
        RecyclerView recyclerViewUser = findViewById(R.id.recycler_view_users);
        recyclerViewUser.setHasFixedSize(true);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerAdapterUsers rList = new RecyclerAdapterUsers(users);
        recyclerViewUser.setAdapter(rList);
        rList.setOnUserClick(this);
    }

    /**
     * Показ диалога создания/редактирования пользователя
     * @param user - выбранный пользователь
     */
    private void showUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Пользователь");

        LayoutInflater inflater = LayoutInflater.from(this);
        View userForm = inflater.inflate(R.layout.content_user, null);
        builder.setView(userForm);

        TextInputEditText loginField = userForm.findViewById(R.id.login);
        TextInputEditText emailField = userForm.findViewById(R.id.email);
        TextInputEditText passwordField = userForm.findViewById(R.id.password);
        SwitchMaterial adminSwitcher = userForm.findViewById(R.id.is_admin);
        SwitchMaterial isVisibleSectionDetailsSwitcher = userForm.findViewById(R.id.is_visible_section_details);
        SwitchMaterial isVisibleSectionWorksSwitcher = userForm.findViewById(R.id.is_visible_section_works);
        SwitchMaterial isVisibleSectionTimesSwitcher = userForm.findViewById(R.id.is_visible_section_times);

        if (user != null) {
            loginField.setText(user.getLogin());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            adminSwitcher.setChecked(Objects.equals(user.getRole(), "admin"));
            isVisibleSectionDetailsSwitcher.setChecked(user.isVisibleSectionDetail());
            isVisibleSectionWorksSwitcher.setChecked(user.isVisibleSectionWorks());
            isVisibleSectionTimesSwitcher.setChecked(user.isVisibleSectionTimes());
        }
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
        // сохранение данных в БД - создание пользователя
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String login = loginField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                boolean isAdmin = adminSwitcher.isChecked();
                boolean isVisibleSectionDetails = isVisibleSectionDetailsSwitcher.isChecked();
                boolean isVisibleSectionWorks = isVisibleSectionWorksSwitcher.isChecked();
                boolean isVisibleSectionTimes = isVisibleSectionTimesSwitcher.isChecked();

                if (DataHandler.isValidEmail(email) && DataHandler.isValidNumber(password) && password.length() > 4) {
                    User item = new User(login, email, password, isVisibleSectionDetails, isVisibleSectionWorks, isVisibleSectionTimes);
                    if (isAdmin) item.setRole("admin");

                    Firebase fb = new Firebase(TABLE, item.getLogin());
                    fb.saveDocument(item);
                    getUsers();
                } else {
                    Snackbar.make(toolbar , "Проверьте введенные данные.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        // отмена
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (user != null) {
            // удаление пользователя
            builder.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    showDeleteDialog(user);
                }
            });
        }

        builder.show();
    }

    /**
     * Удаление пользователя
     * @param user - пользователь
     */
    private void showDeleteDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
        builder.setTitle("Вы уверены?");
        builder.setMessage("Вы уверены, что хотите удалить данные?");
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Firebase fb = new Firebase(TABLE, user.getLogin());
                fb.deleteDocument();
                getUsers();
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
