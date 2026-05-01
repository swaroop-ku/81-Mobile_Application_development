package com.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.newsapp.database.AppDatabase;
import com.newsapp.database.UserDao;
import com.newsapp.R;
import com.newsapp.models.User;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private ProgressBar progressBar;

    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = AppDatabase.getInstance(this).userDao();
        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_signup);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> loginWithEmail());

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void loginWithEmail() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        boolean valid = true;
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email");
            valid = false;
        } else { tilEmail.setError(null); }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Enter password");
            valid = false;
        } else { tilPassword.setError(null); }

        if (!valid) return;

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        User user = userDao.login(email, password);
        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);

        if (user != null) {
            saveSessionAndNavigate(user.getUid(), user.getName(), user.getEmail());
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        }
    }

    private void saveSessionAndNavigate(String uid, String name, String email) {
        sessionManager.setLogin(true);
        sessionManager.saveUserDetails(uid, name != null ? name : "User", email);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
