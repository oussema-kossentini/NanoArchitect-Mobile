package com.example.pidevv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pidevv1.service.UserService;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    private UserService userService;
    private TextInputEditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton selectedGenderRadioButton;
   /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register );


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.context_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
      //  Log.d("tester si le mail est la ", "mail dans le shares prefrence dans profil  : " + email);
        // Initialiser les champs EditText et RadioGroup pour récupérer les informations de l'utilisateur
        firstNameEditText = findViewById(R.id.tiNom);         // Assurez-vous que l'ID correspond à celui dans le fichier XML
        lastNameEditText = findViewById(R.id.tiPrenom);
        emailEditText = findViewById(R.id.tiEmail);
        passwordEditText = findViewById(R.id.tiMotPasse);
        confirmPasswordEditText = findViewById(R.id.tiConfirmationMotPasse);
        genderRadioGroup = findViewById(R.id.radioGroup);     // Le RadioGroup pour sélectionner le sexe

        userService = new UserService(getApplication());  // Initialiser le service utilisateur

        // Gestion de l'image "back" pour revenir à la page de connexion (login)
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Gestion des insets (marges du système)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.context_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gestion du bouton d'inscription
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Récupérer les données saisies par l'utilisateur
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            // Vérifier que les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Les mots de passe ne correspondent pas");
                return;
            }
            Log.d("RegisterActivity", "Bouton S'inscrire cliqué");
            // Vérifier si un genre a été sélectionné
            int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
            if (selectedGenderId == -1) {
                // Aucun genre n'a été sélectionné
                return;
            }

            // Récupérer le genre sélectionné (Homme ou Femme)
            selectedGenderRadioButton = findViewById(selectedGenderId);
            String gender = selectedGenderRadioButton.getText().toString();

            // Appeler la méthode d'enregistrement
            registerUserAndNavigateToProfile(firstName, lastName, email, password, gender);
        });
    }

    private void registerUserAndNavigateToProfile(String firstName, String lastName, String email, String password, String gender) {
        boolean isRegistered = userService.register(firstName, lastName, email, password);  // Ajouter ici l'enregistrement
        if (isRegistered) {
            // Rediriger vers l'écran de profil après une inscription réussie
            Intent intent = new Intent(Register.this, Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Supprimer l'historique des activités
            startActivity(intent);
        } else {
            // Gérer l'échec de l'enregistrement (par exemple, afficher un message d'erreur)
        }
    }

}
