package com.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLogin;
    private ProgressBar progressBar;

    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userDao = AppDatabase.getInstance(this).userDao();
        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.tv_login);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        btnSignup.setOnClickListener(v -> validateAndSignup());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void validateAndSignup() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Name is required");
            valid = false;
        } else { tilName.setError(null); }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email");
            valid = false;
        } else { tilEmail.setError(null); }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            valid = false;
        } else { tilPassword.setError(null); }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            valid = false;
        } else { tilConfirmPassword.setError(null); }

        if (!valid) return;

        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setEnabled(false);

        // Check if user already exists
        User existingUser = userDao.getUserByEmail(email);
        if (existingUser != null) {
            progressBar.setVisibility(View.GONE);
            btnSignup.setEnabled(true);
            tilEmail.setError("Email already registered");
            return;
        }

        // Create new user
        String uid = UUID.randomUUID().toString();
        User user = new User(uid, name, email, password);
        
        userDao.insertUser(user);
        
        progressBar.setVisibility(View.GONE);
        sessionManager.setLogin(true);
        sessionManager.saveUserDetails(uid, name, email);
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
