package com.example.pidev.MainActivity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.pidev.service.UserService;
import com.example.pidev.R;

public class ForgetPassword extends AppCompatActivity {

    private UserService userService;
    private EditText emailInput;
    private EditText codeInput;
    private Button sendButton, confirmButton, resendButton;


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


        // Ensure this matches the layout file name
        setContentView(R.layout.testforget);

        // Initialize UserService
        userService = new UserService(getApplication());

        // Initialize Views
        emailInput = findViewById(R.id.tiEmailForget);
        codeInput = findViewById(R.id.tithecod);
        sendButton = findViewById(R.id.forgetButton);
        confirmButton = findViewById(R.id.df);
        resendButton = findViewById(R.id.resendButton);

        // Initially, disable the resend button until the user sends the email
       resendButton.setEnabled(false);

        // Back Button - to go back to the login screen


        // Send Button - to send the reset code to the user's email
        sendButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgetPassword.this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the method to send the reset code
            boolean result = userService.sendForgetPasswordCode(email);
            if (result) {
                Toast.makeText(ForgetPassword.this, "Code envoyé à votre email", Toast.LENGTH_SHORT).show();

                // Disable the send button and email input field
                sendButton.setEnabled(false);
                emailInput.setEnabled(false); // Prevent email field modification

                // Enable the resend button after a 5-second delay
                enableResendButtonWithDelay();
            } else {
                Toast.makeText(ForgetPassword.this, "Erreur lors de l'envoi du code", Toast.LENGTH_SHORT).show();
            }
        });

        // Confirm Button - to verify the reset code
        confirmButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String code = codeInput.getText().toString().trim();

            if (email.isEmpty() || code.isEmpty()) {
                Toast.makeText(ForgetPassword.this, "Veuillez entrer l'email et le code", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify the reset code
            boolean isCodeValid = userService.verifyResetCode(email, code);
            if (isCodeValid) {
                Toast.makeText(ForgetPassword.this, "Code vérifié avec succès", Toast.LENGTH_SHORT).show();
                // Navigate to ChangePassword activity
                Intent intent = new Intent(ForgetPassword.this, ChangePassword.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                Toast.makeText(ForgetPassword.this, "Code invalide ou expiré", Toast.LENGTH_SHORT).show();
            }
        });

        // Resend Button - to resend the reset code
        resendButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgetPassword.this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Resend the reset code
            boolean result = userService.sendForgetPasswordCode(email);
            if (result) {
                Toast.makeText(ForgetPassword.this, "Code renvoyé à votre email", Toast.LENGTH_SHORT).show();
                // Disable the resend button for 5 seconds after clicking
                resendButton.setEnabled(false);
                enableResendButtonWithDelay();
            } else {
                Toast.makeText(ForgetPassword.this, "Erreur lors de l'envoi du code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to enable the resend button with a 5-second delay
    private void enableResendButtonWithDelay() {
        new Handler().postDelayed(() -> resendButton.setEnabled(true), 5000);
    }
}
