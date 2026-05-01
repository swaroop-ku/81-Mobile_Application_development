package com.example.relativelayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Edge to Edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Linking buttons
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);

        // Click listeners
        btn1.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 1 clicked", Toast.LENGTH_SHORT).show());

        btn2.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 2 clicked", Toast.LENGTH_SHORT).show());

        btn3.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 3 clicked", Toast.LENGTH_SHORT).show());

        btn4.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 4 clicked", Toast.LENGTH_SHORT).show());

        btn5.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 5 clicked", Toast.LENGTH_SHORT).show());

        btn6.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 6 clicked", Toast.LENGTH_SHORT).show());

        btn7.setOnClickListener(v ->
                Toast.makeText(this, "BUTTON 7 clicked", Toast.LENGTH_SHORT).show());
    }
}
