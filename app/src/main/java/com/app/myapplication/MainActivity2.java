package com.app.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Ensure this is the correct layout file
        EdgeToEdge.enable(this);

        Button addButton = findViewById(R.id.button_ajouter); // Reference to the button
        if (addButton == null) {
            Log.e("MainActivity2", "Button with ID 'button_ajouter' not found.");
            return;
        }

        // Set up the button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate back to MainActivity
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setup for 'delete' button
        Button deleteButton = findViewById(R.id.button_delete);
        if (deleteButton != null) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent to navigate to MainActivity4
                    Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e("MainActivity2", "Button with ID 'button_delete' not found.");
        }

    }
}
