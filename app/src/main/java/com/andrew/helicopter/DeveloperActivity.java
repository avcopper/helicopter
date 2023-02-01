package com.andrew.helicopter;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.appcheck.interop.BuildConfig;

public class DeveloperActivity extends BaseActivity {
    Toolbar toolbar;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.menu_developer));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        setText();
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
        appName = findViewById(R.id.app_name);
    }

    /**
     * Установка данных в поля
     */
    private void setText() {
        try {
            appName.setText(
                String.format(
                    getResources().getString(R.string.app_version),
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionName));

//            appName.setText(
//                String.format(getResources().getString(R.string.app_version), BuildConfig.VERSION_NAME)
//            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
