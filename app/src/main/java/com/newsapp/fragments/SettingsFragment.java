package com.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.newsapp.R;
import com.newsapp.activities.LoginActivity;
import com.newsapp.utils.SessionManager;

public class SettingsFragment extends Fragment {

    private Switch switchDarkMode;
    private Button btnLogout;
    private TextView tvVersion;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sessionManager = new SessionManager(requireContext());
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        btnLogout = view.findViewById(R.id.btn_logout);
        tvVersion = view.findViewById(R.id.tv_version);

        tvVersion.setText("Version 1.0.0");

        // Set current dark mode state
        switchDarkMode.setChecked(sessionManager.isDarkMode());

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setDarkMode(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}
