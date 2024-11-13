package com.example.pidev.MainActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pidev.R;
import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.Contrat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    EditText cin_input, type_input, num_input, datestart_input, dateend_input, valeur_input;
    Button add_button;
    Calendar calendar; // Calendar instance to store the selected date

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

        // Initialize the Calendar instance
        calendar = Calendar.getInstance();

        // Set up DatePicker for start date
        datestart_input.setOnClickListener(v -> showDatePickerDialog(datestart_input));

        // Set up DatePicker for end date
        dateend_input.setOnClickListener(v -> showDatePickerDialog(dateend_input));

        // Add button click listener
        add_button.setOnClickListener(view -> addContract());
    }

    private void showDatePickerDialog(final EditText dateInput) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Format selected date and set it to the EditText
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateInput.setText(dateFormat.format(calendar.getTime()));
        };

        // Show DatePickerDialog with the current date as default
        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addContract() {
        // Collecting all inputs and validating
        final String cin = cin_input.getText().toString().trim();
        final String type = type_input.getText().toString().trim();
        final String numStr = num_input.getText().toString().trim();
        final String datestart = datestart_input.getText().toString().trim();
        final String dateend = dateend_input.getText().toString().trim();
        final String valeurStr = valeur_input.getText().toString().trim();

        // Validation checks
        if (cin.isEmpty() || !cin.matches("\\d+")) {
            Toast.makeText(AddActivity.this, "CIN must be a non-empty numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.isEmpty()) {
            Toast.makeText(AddActivity.this, "Type cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (numStr.isEmpty() || !numStr.matches("\\d+")) {
            Toast.makeText(AddActivity.this, "Num must be a non-empty numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (datestart.isEmpty()) {
            Toast.makeText(AddActivity.this, "Start date is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dateend.isEmpty()) {
            Toast.makeText(AddActivity.this, "End date is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (valeurStr.isEmpty() || !valeurStr.matches("\\d+")) {
            Toast.makeText(AddActivity.this, "Valeur must be a non-empty numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parsing numeric inputs after validation
        final int num = Integer.parseInt(numStr);
        final int valeur = Integer.parseInt(valeurStr);

        // Insert into Room database
        new Thread(() -> {
            // Creating a new Contrat object
            Contrat contrat = new Contrat();
            contrat.setCin(cin);
            contrat.setType(type);
            contrat.setNum(num);
            contrat.setDatestart(datestart); // Already formatted as "yyyy-MM-dd"
            contrat.setDateend(dateend);
            contrat.setValeur(valeur);

            // Inserting into the database using the DAO
            AppDataBase db = AppDataBase.getAppDatabase(getApplicationContext());
            db.contratDao().insertContrat(contrat);

            // Finish the activity and go back to MainActivity
            runOnUiThread(this::finish);
        }).start();
    }
}
