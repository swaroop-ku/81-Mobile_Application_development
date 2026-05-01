package com.example.filehandling;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText etData;
    TextView tvOutput;
    Button btnSave, btnLoad;

    String fileName = "myfile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etData = findViewById(R.id.etData);
        tvOutput = findViewById(R.id.tvOutput);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);

        // SAVE DATA
        btnSave.setOnClickListener(v -> {
            String data = etData.getText().toString();

            try {
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();

                Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // LOAD DATA
        btnLoad.setOnClickListener(v -> {
            try {
                FileInputStream fis = openFileInput(fileName);

                int c;
                String temp = "";

                while ((c = fis.read()) != -1) {
                    temp += (char) c;
                }

                tvOutput.setText(temp);

                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}