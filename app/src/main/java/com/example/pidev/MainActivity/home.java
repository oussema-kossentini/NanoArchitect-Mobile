package com.example.pidev.MainActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.example.pidev.service.UserService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pidev.R;

public class home extends AppCompatActivity {

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
