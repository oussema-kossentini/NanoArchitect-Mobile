package com.example.pidev.MainActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pidev.entity.User;
import com.example.pidev.service.UserService;
import com.example.pidev.R;

public class Profile extends AppCompatActivity {
    private UserService userService;

    private EditText emailEditText, firstNameEditText, lastNameEditText, addressEditText, phoneEditText;
//private  SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userService = new UserService(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });}
      //
        //   fama hedi njabha MODE_WORLD_READABLE
    // mPreferences = getSharedPreferences("secret_shared_prefs",1);
       // SharedPreferences mpreferences = PreferenceManager.getDefaultSharedPreferences(context);
      // String x = mPreferences.getString("user_email", "");  // Par défaut, "" si aucune valeur n'est trouvée


       // Log.d("UserService", "Le email dans le stockage local de profil : " + x);
            // Récupérer les vues EditText
        emailEditText = findViewById(R.id.email);
        firstNameEditText = findViewById(R.id.nom);
        lastNameEditText = findViewById(R.id.prenom);
        addressEditText = findViewById(R.id.address);
        phoneEditText = findViewById(R.id.Telephone);

        // Retrieve user information from the service
        User user = userService.getUserInfoFromPreferences();

        // Set the retrieved values in the EditText fields
        if (user.getEmail() != null) {
            emailEditText.setText(user.getEmail());
        }
        if (user.getFirstName() != null) {
            firstNameEditText.setText(user.getFirstName());
        }
        if (user.getLastName() != null) {
            lastNameEditText.setText(user.getLastName());
        }
        if (user.getAdresse() != null) {
            addressEditText.setText(user.getAdresse());
        }
        if (user.getTelephone() != null) {
            phoneEditText.setText(user.getTelephone());
        }
/*
        // mech nekhou les info mech sharede prefrences
      //  SharedPreferences sharedPreferences = getSharedPreferences("secret_shared_prefs", MODE_PRIVATE);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        //this.userService
        String email = sharedPreferences.getString("user_email", "");
        String firstName = sharedPreferences.getString("user_first_name", "");
        String lastName = sharedPreferences.getString("user_last_name", "");
        String address = sharedPreferences.getString("user_adresse", "");
        String phone = sharedPreferences.getString("user_telephone", "");
        Log.d("ProfileActivity", "Email utilisateur : " + email);

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

 */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
