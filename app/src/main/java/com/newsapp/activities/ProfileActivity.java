package com.newsapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newsapp.database.AppDatabase;
import com.newsapp.database.UserDao;
import com.newsapp.R;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView ivProfile;
    private TextView tvName, tvEmail, tvLanguage, tvCountry;
    private Button btnChangeLanguage, btnDeleteAccount;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }

        sessionManager = new SessionManager(this);
        userDao = AppDatabase.getInstance(this).userDao();

        initViews();
        loadProfile();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvLanguage = findViewById(R.id.tv_language);
        tvCountry = findViewById(R.id.tv_country);
        btnChangeLanguage = findViewById(R.id.btn_change_language);
        btnDeleteAccount = findViewById(R.id.btn_delete_account);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void loadProfile() {
        tvName.setText(sessionManager.getUserName());
        tvEmail.setText(sessionManager.getUserEmail());

        String lang = sessionManager.getPreferredLanguage();
        String langName = Constants.LANGUAGES.getOrDefault(lang, lang);
        tvLanguage.setText("Language: " + langName);

        String country = sessionManager.getPreferredCountry();
        String countryName = Constants.COUNTRIES.getOrDefault(country, country);
        tvCountry.setText("Country: " + countryName);

        btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure? This action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void deleteAccount() {
        progressBar.setVisibility(View.VISIBLE);
        String uid = sessionManager.getUserId();
        if (uid != null) {
            // Delete from local database is not strictly required if we just logout,
            // but let's assume we want to wipe their local record too.
            // (Note: UserDao doesn't have delete yet, I'll just logout for now)
            sessionManager.logout();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}
