package com.example.pidevv1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    private EditText emailEditText, firstNameEditText, lastNameEditText, addressEditText, phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Récupérer les vues EditText
        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.nom);
        lastNameEditText = findViewById(R.id.prenom);
        addressEditText = findViewById(R.id.address);
        phoneEditText = findViewById(R.id.Telephone);

        // Récupérer les informations utilisateur des SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("secret_shared_prefs", MODE_PRIVATE);

        String email = sharedPreferences.getString("user_email", "");
        String firstName = sharedPreferences.getString("user_first_name", "");
        String lastName = sharedPreferences.getString("user_last_name", "");
        String address = sharedPreferences.getString("user_adresse", "");
        String phone = sharedPreferences.getString("user_telephone", "");

        // Remplir les champs avec les informations utilisateur
        emailEditText.setText(email);
        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        addressEditText.setText(address);
        phoneEditText.setText(phone);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
