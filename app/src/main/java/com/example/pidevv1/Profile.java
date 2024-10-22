package com.example.pidevv1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    private EditText emailEditText, firstNameEditText, lastNameEditText, addressEditText, phoneEditText;
private  SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        View mainView = findViewById(R.id.main); // Doit se produire après setContentView
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });}
        mPreferences = getSharedPreferences("secret_shared_prefs", MODE_PRIVATE);
        String x = mPreferences.getString("user_email", "");  // Par défaut, "" si aucune valeur n'est trouvée
        Log.d("UserService", "Le email dans le stockage local de profil : " + x);
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
        Log.d("ProfileActivity", "Email utilisateur : " + email);
        // Remplir les champs avec les informations utilisateur
        if (email != null) {
            emailEditText.setText(email);
        }
        if (firstName != null) {
            firstNameEditText.setText(firstName);
        }
        if (lastName != null) {
            lastNameEditText.setText(lastName);
        }
        if (address != null) {
            addressEditText.setText(address);
        }
        if (phone != null) {
            phoneEditText.setText(phone);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
