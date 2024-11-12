package com.example.pidev.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.Contrat;
//import com.java.akrem.contrat.R;
import com.example.pidev.R;
public class AddActivity extends AppCompatActivity {

    EditText cin_input, type_input, num_input, datestart_input, dateend_input, valeur_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        cin_input = findViewById(R.id.cin_input);
        type_input = findViewById(R.id.type_input);
        num_input = findViewById(R.id.num_input);
        datestart_input = findViewById(R.id.datestart_input);
        dateend_input = findViewById(R.id.dateend_input);
        valeur_input = findViewById(R.id.valeur_input);
        add_button = findViewById(R.id.add_button);

        // When add button is clicked
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Collecting all inputs
                final String cin = cin_input.getText().toString().trim();
                final String type = type_input.getText().toString().trim();
                final int num = Integer.parseInt(num_input.getText().toString().trim());
                final String datestart = datestart_input.getText().toString().trim();
                final String dateend = dateend_input.getText().toString().trim();
                final int valeur = Integer.parseInt(valeur_input.getText().toString().trim());

                // Insert into Room database
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Creating a new Contrat object
                        Contrat contrat = new Contrat();
                        contrat.setCin(cin);
                        contrat.setType(type);
                        contrat.setNum(num);
                        contrat.setDatestart(datestart);
                        contrat.setDateend(dateend);
                        contrat.setValeur(valeur);

                        // Inserting into the database using the DAO
                        AppDataBase db = AppDataBase.getAppDatabase(getApplicationContext());
                        db.contratDao().insertContrat(contrat);

                        // Finish the activity and go back to MainActivity
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();  // This will return you to MainActivity
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
