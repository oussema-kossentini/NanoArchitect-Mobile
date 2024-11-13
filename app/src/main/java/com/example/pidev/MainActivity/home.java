package com.example.pidev.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.example.pidev.service.UserService;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.pidev.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class home extends AppCompatActivity {
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Check if user is logged in
        //   SharedPreferences sharedPreferences = getSharedPreferences("com.example.pidevv1", MODE_PRIVATE);
        // Vérification de la présence du jeton JWT dans EncryptedSharedPreferences
        String jwtToken = mPreferences.getString("jwt_token", null);
        if (jwtToken == null) {
            // Si aucun jeton n'est trouvé, rediriger vers l'activité Login
            Intent intent = new Intent(home.this, Login.class);
            startActivity(intent);
            finish(); // Fermer MainActivity pour éviter qu'elle reste dans la pile
            return; // Sortie anticipée de onCreate car l'utilisateur est redirigé
        }
    /*    else{Intent intent = new Intent(home.this, home.class);
            startActivity(intent);}
        */
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

        setContentView(R.layout.activity_home);

        // Adjust window insets for proper padding on the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the gestioncontrat button and set the click listener
        CardView gestionContratButton = findViewById(R.id.contrats);
        gestionContratButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to MainActivity
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Find the profilclient CardView and set the click listener
        CardView user = findViewById(R.id.profilclient);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Profile activity
                Intent intent = new Intent(home.this, Profile.class);
                startActivity(intent);
            }
        });


        // Find the blog CardView and set the click listener
        CardView blog = findViewById(R.id.blog);
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(home.this, MainActivityRihab.class);
                startActivity(intent);
            }
        });




//        //Deconnexion
//        CardView Deconnexion = findViewById(R.id.Deconnexion);
//        Deconnexion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(home.this, Login.class);
//                startActivity(intent);
//            }
//        });
        // Assuming UserService is already instantiated somewhere in your activity
        UserService userService = new UserService(getApplication());
        // Configuration de l'action sur retour en arrière
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Mettre l'application en arrière-plan
                moveTaskToBack(true);
            }
        });
// Deconnexion

        CardView Deconnexion = findViewById(R.id.Deconnexion);
        Deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the logout service
                userService.logout();

                // Navigate to Login Activity
                Intent intent = new Intent(home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears the back stack
                startActivity(intent);

                // Optionally, finish the current activity to prevent going back
                finish();
            }
        });


    }
}
