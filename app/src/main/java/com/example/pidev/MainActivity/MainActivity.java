package com.example.pidev.MainActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    CustomAdapter customAdapter;
    List<Contrat> contratList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*     try {
            // Création de la clé maître (MasterKey) en utilisant MasterKey.Builder
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Initialisation de EncryptedSharedPreferences
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    this,// Le contexte
                    "secret_shared_prefs",  // Nom du fichier SharedPreferences
                    masterKey,               // L'objet MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,   // Schéma pour les clés
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM  // Schéma pour les valeurs
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("login", "user123");
            editor.apply();
            mPreferences = sharedPreferences;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // Check if user is logged in
     //   SharedPreferences sharedPreferences = getSharedPreferences("com.example.pidevv1", MODE_PRIVATE);
        // Vérification de la présence du jeton JWT dans EncryptedSharedPreferences
        String jwtToken = mPreferences.getString("jwt_token", null);
        if (jwtToken == null) {
            // Si aucun jeton n'est trouvé, rediriger vers l'activité Login
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish(); // Fermer MainActivity pour éviter qu'elle reste dans la pile
            return; // Sortie anticipée de onCreate car l'utilisateur est redirigé
        }
        else{Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);}*/
        setContentView(R.layout.activity_main);
//
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
