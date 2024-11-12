package com.example.pidev.MainActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.Contrat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.pidev.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    CustomAdapter customAdapter;
    List<Contrat> contratList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.pidevv1", MODE_PRIVATE);
        if (sharedPreferences.getString("login", null) == null) {
            // If no login information found, redirect to Login activity
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish(); // Close the MainActivity so it doesn't remain in the back stack
            return; // Exit onCreate early since the user is redirected
        }

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);  // Start AddActivity expecting result
            }
        });

        // Initialize the list that will store all contracts
        contratList = new ArrayList<>();

        // Fetch data from Room database and display it
        storeDataInList();

        customAdapter = new CustomAdapter(MainActivity.this, this, contratList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Reload the data when AddActivity finishes successfully
            storeDataInList();  // Fetch the updated data
        }
    }

    // Optional: Refresh data when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        storeDataInList();
    }

    // Fetch data from Room database
    void storeDataInList() {
        // Get the Room database instance
        AppDataBase db = AppDataBase.getAppDatabase(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get all contracts from the Room database
                List<Contrat> contrats = db.contratDao().getAllContrats();

                if (contrats.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            empty_imageview.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    contratList.clear();  // Clear the list to avoid duplication
                    contratList.addAll(contrats);  // Add all contracts to the list

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            empty_imageview.setVisibility(View.GONE);
                            no_data.setVisibility(View.GONE);
                            customAdapter.notifyDataSetChanged();  // Notify adapter of data changes
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer tout ?");
        builder.setMessage("Êtes-vous sûr de vouloir supprimer toutes les données ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Delete all data from Room database
                        AppDataBase db = AppDataBase.getAppDatabase(getApplicationContext());
                        db.contratDao().deleteAllContrats();

                        // Refresh Activity after deletion
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                contratList.clear();  // Clear the list in the UI
                                customAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If the user cancels the action
            }
        });
        builder.create().show();
    }
}
