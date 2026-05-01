package com.example.absolutelayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnCancel = findViewById(R.id.btnCancel);

        btnSignIn.setOnClickListener(v ->
                Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT).show()
        );

        btnCancel.setOnClickListener(v ->
                Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_SHORT).show()
        );
    }
}
