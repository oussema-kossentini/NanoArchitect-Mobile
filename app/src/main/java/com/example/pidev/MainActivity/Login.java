package com.example.pidev.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.pidev.service.UserService;
import com.example.pidev.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Login extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private EditText mLogin;
    private EditText mPassword;
    private static final String sharedPrefFile = "com.example.pidevv1";

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

        setContentView(R.layout.llogin);
        UserService userService = new UserService(getApplication());
        /*
// Initialisation de SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        mLogin = findViewById(R.id.tiEmail); // ID du champ de login
        mPassword = findViewById(R.id.tiPassword); // ID du champ de mot de passe

// Récupérer les données sauvegardées si elles existent
        mLogin.setText(mPreferences.getString("login", ""));
        mPassword.setText(mPreferences.getString("password", ""));
*/


        try {
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

        mLogin = findViewById(R.id.tiEmail); // ID du champ de login
        mPassword = findViewById(R.id.tiPassword); // ID du champ de mot de passe

        // Récupérer les données chiffrées si elles existent
        mLogin.setText(mPreferences.getString("login", ""));
        mPassword.setText(mPreferences.getString("password", ""));

        // Sauvegarder les données chiffrées lors de la connexion

        Button loginButton = findViewById(R.id.loginButton);


        mLogin = findViewById(R.id.tiEmail);  // ID du champ email
        mPassword = findViewById(R.id.tiPassword);  // ID du champ mot de passe


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer l'email et le mot de passe
                String email = mLogin.getText().toString();
                String password = mPassword.getText().toString();

                // Appeler le service pour la connexion
                boolean isAuthenticated = userService.seConnecter(email, password);

              /*  if (isAuthenticated) {
                    // Connexion réussie, redirection vers l'activité principale
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
*/
                    if (isAuthenticated) {
                        // Redirect to the "home" activity on successful login
                        Intent intent = new Intent(Login.this, home.class);
                        startActivity(intent);
                        finish(); //

                } else {
                    // Connexion échouée, afficher un message d'erreur
                    Toast.makeText(Login.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // ki  tenzel  ala mot de pass oublier
        TextView forgetPassword = findViewById(R.id.forgetpassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers la page "Mot de passe oublié"
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        // ki tenzel ala cree un compte
        TextView signupText = findViewById(R.id.signupTextCombined);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers la page  mtaa cree un compte
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Gestion des insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}


