package com.example.notifications;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btnNotify);

        // Button click → show dialog
        btnNotify.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Title
        builder.setTitle("Notification of Application");

        // Message
        builder.setMessage("This is my first push notification");

        // OK button
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        // Show dialog
        builder.show();
    }
}