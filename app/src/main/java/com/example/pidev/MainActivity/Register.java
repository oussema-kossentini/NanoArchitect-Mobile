package com.example.pidev.MainActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pidev.service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.example.pidev.R;

public class Register extends AppCompatActivity {
    private CountryCodePicker countryCodePicker;
    private UserService userService;
    private TextInputEditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText,AdressEditText,telephoneEditText;
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
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        setContentView(R.layout.register);
      //  Log.d("tester si le mail est la ", "mail dans le shares prefrence dans profil  : " + email);
        // Initialiser les champs EditText et RadioGroup pour récupérer les informations de l'utilisateur
        firstNameEditText = findViewById(R.id.tiNom);         // Assurez-vous que l'ID correspond à celui dans le fichier XML
        lastNameEditText = findViewById(R.id.tiPrenom);
        emailEditText = findViewById(R.id.tiEmail);
        passwordEditText = findViewById(R.id.tiMotPasse);
        confirmPasswordEditText = findViewById(R.id.tiConfirmationMotPasse);
        AdressEditText = findViewById(R.id.tiAdresse);
        telephoneEditText=findViewById(R.id.tiTelephone);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        genderRadioGroup = findViewById(R.id.radioGroup);     // Le RadioGroup pour sélectionner le sexe



     /*   TextInputEditText tiNom = findViewById(R.id.tiNom);
        TextInputEditText tiPrenom = findViewById(R.id.tiPrenom);
        TextInputEditText tiEmail = findViewById(R.id.tiEmail);
        TextInputEditText tiMotPasse = findViewById(R.id.tiMotPasse);
        TextInputEditText tiConfirmationMotPasse = findViewById(R.id.tiConfirmationMotPasse);
        TextInputEditText tiAdresse = findViewById(R.id.tiAdresse);
        TextInputEditText tiTelephone = findViewById(R.id.tiTelephone);*/




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
            String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
String adress = AdressEditText.getText().toString();
String phonne = telephoneEditText.getText().toString();
            // Vérifier que les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Les mots de passe ne correspondent pas");
                return;
            }
            String phone = countryCode + phonne;
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty() || confirmPassword.isEmpty()) {
                passwordEditText.setError("mdp est  obligatoir");
                Toast.makeText(this, "mdp est  obligatoir", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                telephoneEditText.setError("phone obligatoir");
                Toast.makeText(this, "phone obligatoir 2", Toast.LENGTH_SHORT).show();
                return;
            }
            if (firstName.isEmpty()) {
                firstNameEditText.setError("firstname obligatoir");
                return;
            }
            if (lastName.isEmpty()) {
                lastNameEditText.setError("lastname est obligatoir");
                return;
            }
            if (email.isEmpty()) {
                emailEditText.setError("email est obligatoir");
                return;
            }

            if (adress.isEmpty()) {
                AdressEditText.setError("adress est obligatoir");


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
            registerUserAndNavigateToProfile(firstName, lastName, email, password, gender,phone,adress);
        });
    }

    private void registerUserAndNavigateToProfile(String firstName, String lastName, String email, String password, String gender,String phone,String adress) {


        boolean isRegistered = userService.register(firstName, lastName, email, password,gender,phone,adress);  // Ajouter ici l'enregistrement
        if (isRegistered) {
            // Rediriger vers l'écran de home après une inscription réussie
            Intent intent = new Intent(Register.this, home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Supprimer l'historique des activités
            startActivity(intent);
        } else {
            Toast.makeText(Register.this, "Erreur creation de compt ", Toast.LENGTH_SHORT).show();
        }
    }

}
