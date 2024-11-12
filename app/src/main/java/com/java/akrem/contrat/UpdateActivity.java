package com.java.akrem.contrat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.java.akrem.contrat.database.AppDatabase;
import com.java.akrem.contrat.entity.Contrat;

public class UpdateActivity extends AppCompatActivity {

    EditText cin_input, type_input, num_input, datestart_input, dateend_input, valeur_input;
    Button update_button, delete_button;

    String id, cin, type, num, datestart, dateend, valeur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        cin_input = findViewById(R.id.cin_input2);
        type_input = findViewById(R.id.type_input2);
        num_input = findViewById(R.id.num_input2);
        datestart_input = findViewById(R.id.datestart_input2);
        dateend_input = findViewById(R.id.dateend_input2);
        valeur_input = findViewById(R.id.valeur_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // First we call this to set the data from intent
        getAndSetIntentData();

        // Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(cin);
        }

        // Update button click listener
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Updating the entry using Room
                cin = cin_input.getText().toString().trim();
                type = type_input.getText().toString().trim();
                num = num_input.getText().toString().trim();
                datestart = datestart_input.getText().toString().trim();
                dateend = dateend_input.getText().toString().trim();
                valeur = valeur_input.getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Fetching database instance and updating data
                        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                        Contrat contrat = new Contrat();
                        contrat.setId(Integer.parseInt(id));  // Setting the ID for update
                        contrat.setCin(cin);
                        contrat.setType(type);
                        contrat.setNum(Integer.parseInt(num));
                        contrat.setDatestart(datestart);
                        contrat.setDateend(dateend);
                        contrat.setValeur(Integer.parseInt(valeur));

                        db.contratDao().updateContrat(contrat);

                        // Optional: You can add logic to return to MainActivity or show a confirmation
                    }
                }).start();
            }
        });

        // Delete button click listener
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();  // Call dialog confirmation before deleting
            }
        });
    }

    // Fetching and setting data from the Intent
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("cin") &&
                getIntent().hasExtra("type") && getIntent().hasExtra("num")){
            // Getting data from Intent
            id = getIntent().getStringExtra("id");
            cin = getIntent().getStringExtra("cin");
            type = getIntent().getStringExtra("type");
            num = getIntent().getStringExtra("num");
            datestart = getIntent().getStringExtra("datestart");
            dateend = getIntent().getStringExtra("dateend");
            valeur = getIntent().getStringExtra("valeur");

            // Setting Intent data to the input fields
            cin_input.setText(cin);
            type_input.setText(type);
            num_input.setText(num);
            datestart_input.setText(datestart);
            dateend_input.setText(dateend);
            valeur_input.setText(valeur);
            Log.d("stev", cin + " " + type + " " + num);
        } else {
            Toast.makeText(this, "Aucune donnée.", Toast.LENGTH_SHORT).show();
        }
    }

    // Confirmation dialog before deletion
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer " + cin + " ?");
        builder.setMessage("Êtes-vous sûr de vouloir supprimer " + cin + " ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Fetching database instance and deleting the entry
                        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                        Contrat contrat = new Contrat();
                        contrat.setId(Integer.parseInt(id));

                        db.contratDao().deleteContrat(contrat);

                        // Returning to MainActivity after deletion
                        finish();
                    }
                }).start();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If the user cancels deletion
            }
        });
        builder.create().show();
    }
}
