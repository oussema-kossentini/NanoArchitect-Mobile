package com.example.pidev.MainActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pidev.R;
import com.example.pidev.service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {







    private TextInputEditText emailInput;
    private TextInputEditText newPasswordInput;
    private TextInputEditText confirmPasswordInput;
    private Button changePasswordButton;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        userService = new UserService(getApplication());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initialize Views
        emailInput = findViewById(R.id.tiEmailForget);
        newPasswordInput = findViewById(R.id.tiMotPasse);
        confirmPasswordInput = findViewById(R.id.tiConfirmationMotPasse);
        changePasswordButton = findViewById(R.id.forgetButton);
        String email = getIntent().getStringExtra("email");
        emailInput.setText(email);
        emailInput.setEnabled(false);
        // Set up the back button functionality
        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        // Set click listener on the change password button
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String email = emailInput.getText().toString().trim();
                String newPassword = newPasswordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                if (validateInputs( newPassword, confirmPassword)) {
                    boolean result = userService.changePassword(email, newPassword);
                    if (result) {
                        Toast.makeText(ChangePassword.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ChangePassword.this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        // Navigate back to Login
        Intent intent = new Intent(ChangePassword.this, Login.class);
        startActivity(intent);
        finish(); // Close ChangePasswordActivity
        return true;
    }
    // Helper function to validate inputs
    private boolean validateInputs( String newPassword, String confirmPassword) {


        if (newPassword.isEmpty()) {
            newPasswordInput.setError("New password is required");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}
