package com.example.inputcontrols1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    CheckBox checkBox1;
    RadioGroup radioGroup;
    ToggleButton toggleButton;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // CheckBox
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(v -> {
            if (checkBox1.isChecked()) {
                Toast.makeText(MainActivity.this, "Mobile Technology Selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Mobile Technology Unselected", Toast.LENGTH_SHORT).show();
            }
        });

        // RadioGroup
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            Toast.makeText(MainActivity.this,
                    "Selected: " + radioButton.getText(),
                    Toast.LENGTH_SHORT).show();
        });

        // ToggleButton
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(v -> {
            if (toggleButton.isChecked()) {
                Toast.makeText(MainActivity.this, "Toggle is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Toggle is OFF", Toast.LENGTH_SHORT).show();
            }
        });

        // Spinner
        spinner = findViewById(R.id.sp1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.names,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}