package com.andrew.helicopter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.andrew.helicopter.Models.Detail;
import com.andrew.helicopter.Models.Group;
import com.andrew.helicopter.Models.Helicopter;
import com.andrew.helicopter.Models.Time;
import com.andrew.helicopter.Models.User;
import com.andrew.helicopter.Models.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    User currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActionBar actionBar;
    Toolbar toolbar;
    Helicopter helicopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helicopter = new Helicopter();
        currentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        //getBackUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(currentUser != null && currentUser.isAdmin() ? R.menu.admin : R.menu.main, menu);

        if (!currentUser.isVisibleSectionDetail()) menu.findItem(R.id.action_list).setVisible(false);
        if (!currentUser.isVisibleSectionWorks()) menu.findItem(R.id.action_work).setVisible(false);
        if (!currentUser.isVisibleSectionTimes()) menu.findItem(R.id.action_time).setVisible(false);

        return true;
    }

    /**
     * ?????????????????? ?????????? ???? ?????????????? ???????????????? ????????
     * @param item - ?????????????????? ??????????
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_list: {
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("HELICOPTER", helicopter);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_time: {
                Intent intent = new Intent(this, TimeListActivity.class);
                intent.putExtra("HELICOPTER", helicopter);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_work: {
                Intent intent = new Intent(this, WorkListActivity.class);
                intent.putExtra("HELICOPTER", helicopter);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_users: {
                Intent intent = new Intent(this, UsersActivity.class);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_developer: {
                Intent intent = new Intent(this, DeveloperActivity.class);
                intent.putExtra("CURRENT_USER", currentUser);
                startActivity(intent);
                break;
            }
            case R.id.action_search: {
                Toast.makeText(
                    this,
                    (helicopter == null) ? String.valueOf(helicopter) : "helicopter: " + helicopter.getNumber(),
                    Toast.LENGTH_SHORT
                ).show();
                break;
            }
            case R.id.action_exit: {
                Snackbar.make(toolbar ,"???? ???????????????", Snackbar.LENGTH_INDEFINITE)
                    .setAction("??????????" , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                break;
            }
        }

        return true;
    }

    /**
     * ?????????????????? ??????????????????
     * @param actionBar - ????????????
     * @param text - ?????????? ??????????????????
     */
    protected void setTitle(ActionBar actionBar, String text) {
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.title) + " - " + getString(R.string.bort) + " " + text);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    /**
     * ???????????????? ???????????? ????????
     */
    public void getBackUp() {
        getHelicoptersJson();
        getGroupsJson();
        getTimesJson();
        getUsersJson();
    }

    /**
     * ?????????????? ?????????? ???????????? ???????????????????? ?? json-??????????????
     */
    public void getHelicoptersJson() {
        db.collection("helicopters")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> h = new ArrayList<>();
                        h.add("template");
                        String json = "{\"helicopters\": [";

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Helicopter item = document.toObject(Helicopter.class);
                            h.add(item.getNumber());
                            json += (item.toJson() + ",");
                        }

                        json = json.substring(0, json.length() - 1);
                        json += "]}";

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "helicopters.json"));
                            fileOutputStream.write(json.getBytes());
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        getDetailsJson(h);
                        h.set(0, "Template");
                        getWorksJson(h);
                    }
                }
            });
    }

    /**
     * ?????????????? ?????????? ???????????? ?????????????? ?? json-??????????????
     * @param h - ???????????? ????????????????????
     */
    public void getDetailsJson(ArrayList<String> h) {
        for (int i = 0; i < h.size(); i++) {
            String num = h.get(i);

            db.collection(num)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String json = "{\"" + num + "\": [";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Detail item = document.toObject(Detail.class);
                                json += (item.toJson() + ",");
                            }

                            json = json.substring(0, json.length() - 1);
                            json += "]}";

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), num + ".json"));
                                fileOutputStream.write(json.getBytes());
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        }
    }

    /**
     * ?????????????? ?????????? ???????????? ?????????????????????????? ?????????? ?? json-??????????????
     * @param h - ???????????? ????????????????????
     */
    public void getWorksJson(ArrayList<String> h) {
        for (int i = 0; i < h.size(); i++) {
            String num = h.get(i);

            db.collection("works" + num)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String json = "{\"" + "works" + num + "\": [";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Work item = document.toObject(Work.class);
                                json += (item.toJson() + ",");
                            }

                            json = json.substring(0, json.length() - 1);
                            json += "]}";

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "works" + num + ".json"));
                                fileOutputStream.write(json.getBytes());
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        }
    }

    /**
     * ?????????????? ?????????? ???????????? ?????????? ?? json-??????????????
     */
    public void getGroupsJson() {
        db.collection("groups")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> h = new ArrayList<>();
                        String json = "{\"groups\": [";

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Group item = document.toObject(Group.class);

                            json += (item.toJson() + ",");
                        }

                        json = json.substring(0, json.length() - 1);
                        json += "]}";

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "groups.json"));
                            fileOutputStream.write(json.getBytes());
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
    }

    /**
     * ?????????????? ?????????? ???????????? ?????????????????????? ?????????? ?? json-??????????????
     */
    public void getTimesJson() {
        db.collection("times")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> h = new ArrayList<>();
                        String json = "{\"times\": [";

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String number = document.getId();
                            json += ("{\"id\": \"" + number + "\"}" + ",");
                            h.add(number);
                        }

                        json = json.substring(0, json.length() - 1);
                        json += "]}";

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "times.json"));
                            fileOutputStream.write(json.getBytes());
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        getTimesListJson(h);
                    }
                }
            });
    }

    /**
     * ?????????????? ?????????? ???????????? ?????????????????? ?????????????????? ?????????????????????? ?????????? ?? json-??????????????
     * ?? ??????????????????, ?? ?????????????????? ?????????? (01.2023) ?????? ?????????????????????? ???????????????? ???????????? ?????????????????? ?????????????????? ???? ????????
     * ???????????? ???????????????????? ???????????????????? ?????????????????? ???????????????? ?? ????????????
     * @param numbers - ???????????? ????????????????????
     */
    public void getTimesListJson(ArrayList<String> numbers) {
        for (int n = 0; n < numbers.size(); n++) {
            String number = numbers.get(n);
            int[] years = {2022, 2023};

            for (int year : years) {
                for (int month = 1; month <= 12; month++) {
                    String monthYear = (month < 10 ? ("0" + month) : month) + "." + year;
                    String collection = "times/" + number + "/" + monthYear;

                    db.collection(collection)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String json = "{\"" + monthYear + "\": [";
                                    String items = "";

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Time item = document.toObject(Time.class);
                                        items += (item.toJson() + ",");
                                    }

                                    if (!items.equals("")) {
                                        items = items.substring(0, items.length() - 1);
                                        json += (items + "]}");

                                        try {
                                            String storageDirectory = getFilesDir().getAbsolutePath();
                                            File folder = new File(storageDirectory, "times/" + number);
                                            if (!folder.isDirectory()) folder.mkdirs();

                                            FileOutputStream fileOutputStream = new FileOutputStream(new File(folder, monthYear + ".json"));
                                            fileOutputStream.write(json.getBytes());
                                            fileOutputStream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                }
            }
        }
    }

    /**
     * ?????????????? ?????????? ??????????????????????????
     * TODO ????????????????!
     */
    private void getUsersJson() {
        db.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        String json = "{\"users\": [";

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User item = document.toObject(User.class);
                            json += (item.toJson() + ",");
                        }

                        json = json.substring(0, json.length() - 1);
                        json += "]}";

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(getFilesDir(), "users.json"));
                            fileOutputStream.write(json.getBytes());
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}
