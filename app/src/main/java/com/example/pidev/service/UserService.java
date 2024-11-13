package com.example.pidev.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.pidev.dao.UserDao;
import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.User;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserService {
    private UserDao userDao;
    private SharedPreferences mPreferences;
    private List<String> tokenBlacklist = new ArrayList<>();

    public UserService(Application application) {
        AppDataBase db = AppDataBase.getAppDatabase(application);
        this.userDao = db.userDao();

        try {
            MasterKey masterKey = new MasterKey.Builder(application)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            mPreferences = EncryptedSharedPreferences.create(
                    application,
                    "secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
    public String getTokenFromPreferences() {
        return mPreferences.getString("jwt_token", "");
    }
    public User getUserInfoFromPreferences() {
        String email = mPreferences.getString("user_email", "");
        String firstName = mPreferences.getString("user_first_name", "");
        String lastName = mPreferences.getString("user_last_name", "");
        String address = mPreferences.getString("user_adresse", "");
        String phone = mPreferences.getString("user_telephone", "");

        // Create and return a User object with the retrieved values
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAdresse(address);
        user.setTelephone(phone);

        return user;
    }
    // create a user
    public boolean register(String firstName, String lastName, String email, String password,String gender, String phone ,String adress) {
        List<User> existingUsers = userDao.getUserByEmail(email);
        if (existingUsers != null && !existingUsers.isEmpty()) {
            return false;
        }
        //userService.register(firstName, lastName, email, password,gender,phone,adress);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setRole("user");
newUser.setGenre(gender);
newUser.setAdresse(adress);
newUser.setTelephone(phone);
        new Thread(() -> userDao.insetOne(newUser)).start();
        Log.d("UserService", "Utilisateur enregistré : " + email);
        String token = generateToken(newUser.getUid());


        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();
        getUserInfo(email);
        return true;
    }
    public void getUserInfo(String email) {
        List<User> users = userDao.getUserByEmail(email);
        if (users != null && users.size() == 1) {
            User user = users.get(0);
            Log.d("UserService", "Utilisateur trouvé : " + user.getEmail());
            // Stocker les informations utilisateur dans les SharedPreferences
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("user_id", String.valueOf(user.getUid()));
            editor.putString("user_first_name", user.getFirstName());
            editor.putString("user_last_name", user.getLastName());
            editor.putString("user_email", user.getEmail());
            editor.putString("user_role", user.getRole());
            Log.d("UserService", "le email est  : " + email);
            editor.putString("user_genre", user.getGenre());
            editor.putString("user_adresse", user.getAdresse());
            editor.putString("user_telephone", user.getTelephone());
            editor.apply();
            String x = mPreferences.getString("user_email", "");
            Log.d("UserService", "Le email dans le stockage local est : " + x);
        } else {
            Log.e("UserService", "User not found");
        }
    }
    // login iverfife fel baz ou yaati token ou yaapeli service mech ihot les donner fel stockage local
    public boolean seConnecter(String email, String password) {
        List<User> users = userDao.getUserByEmail(email);
        if (users != null && users.size() == 1) {
            User user = users.get(0);

            // Vérifier el mot de pass
            if (BCrypt.checkpw(password, user.getPassword())) {
                String token = generateToken(user.getUid());

                // stocker token fi local storage
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("jwt_token", token);
                editor.apply();
                getUserInfo(email);
                return true;
            }
        }
        return false;
    }


   /* public void logout() {
        String token = mPreferences.getString("jwt_token", null);
        if (token != null) {
            tokenBlacklist.add(token); // Ajouter le token à la blacklist
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.remove("jwt_token"); // Supprimer le token des préférences partagées
            editor.apply();
        }
    }*/
   public void logout() {
       // Retrieve the JWT token from SharedPreferences
       String token = mPreferences.getString("jwt_token", null);

       // If the token exists, proceed to blacklist it and remove it from SharedPreferences
       if (token != null) {
           // Add the token to the blacklist (assuming tokenBlacklist is a predefined collection or method)
           tokenBlacklist.add(token);

           // Begin editing SharedPreferences
           SharedPreferences.Editor editor = mPreferences.edit();

           // Remove the JWT token from SharedPreferences
           editor.remove("jwt_token");

           // Clear all SharedPreferences data (optional, if you want to clear all data)
           editor.clear();

           // Apply the changes to SharedPreferences
           editor.apply();
       }
   }


    // tesnaa el token
    private String generateToken(int userId) {
        Algorithm algorithm = Algorithm.HMAC256("5aDx2Kf9MlXhePbc5TdzLop8uHmn76V5r0qA2FHjYEQ=");
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (10 * 60 * 60 * 1000)); // Token expire après 10 heures

        return JWT.create()
                .withClaim("userId", userId)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    // Vérifier si un token est blacklisté mbaad mech nged fazzet el bann
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
// ya bel mail ya bel tilifoun
    public boolean verifyResetCode(String email, String code) {


        List<User> users = userDao.getUserByEmail(email);

        if (users.isEmpty()) {
            return false;
        }

        User user = users.get(0);
        long currentTimeMillis = System.currentTimeMillis(); // Obtenir l'heure actuelle en millisecondes lel fazet delai mtaa token

        // Récupérer le temps d'expiration stocké  stokit az long,
        long expirationTimeMillis = user.getResetTokenExpiration();

        // Comparer les timestamps pour vérifier l'expiration
        boolean tokenNotExpired = currentTimeMillis < expirationTimeMillis;

        // Vérifier si jawou bahi
        boolean tokenMatches = user.getTokenForgetPassword().equals(code);

        return tokenMatches && tokenNotExpired;
    }


    public boolean sendForgetPasswordCode(String email) {
        try {
            List<User> users = userDao.getUserByEmail(email);

            if (users == null || users.isEmpty()) {
                return false; // The user does not exist
            }

            User user = users.get(0);
            String resetCode = generateResetCode();
            if (resetCode == null) {
                return false; // Error in code generation
            }

            // Set expiration time for the token (10 minutes)
            long expirationTimeMillis = System.currentTimeMillis() + (10 * 60 * 1000);

            user.setTokenForgetPassword(resetCode);
            user.setResetTokenExpiration(expirationTimeMillis);

            // Update the database in a separate thread
            new Thread(() -> {
                try {
                    userDao.updateForgetPassword(user.getTokenForgetPassword(), expirationTimeMillis, email);
                } catch (Exception e) {
                    Log.e("Error", "Failed to update user", e);
                }
            }).start();

            // Use an ExecutorService to send email in the background
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> sendEmail(email, resetCode));
            executorService.shutdown();

            return true;
        } catch (Exception e) {
            Log.e("Error", "Failed to send forget password code", e);
            return false;
        }
    }

    public void sendEmail(String recipientEmail, String resetCode) {
        final String username = "oussema.kossentini@gmail.com"; // Your Gmail address
        final String password = "lfzsgyruoqnzoiru"; // Your Gmail app-specific password

        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a new session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Réinitialisation de votre mot de passe");
            message.setText("Votre code de réinitialisation de mot de passe est : "
                    + resetCode
                    + "\nCe code expirera dans 10 minutes.");

            // Send the email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

  /*  public boolean sendForgetPasswordCode(String email) {
        // Exécuter en tâche de fond
        List<User> users = userDao.getUserByEmail(email);

        if (users == null || users.isEmpty()) {
            return false; // L'utilisateur n'existe pas
        }

        User user = users.get(0);
        String resetCode = generateResetCode();

        // Calculer l'expiration du token en millisecondes (10 minutes à partir de maintenant)
        long expirationTimeMillis = System.currentTimeMillis() + (10 * 60 * 1000); // 10 minutes

        user.setTokenForgetPassword(resetCode);
        user.setResetTokenExpiration(expirationTimeMillis); // Stocker le timestamp en millisecondes

        // Mettre à jour la base de données dans un thread séparé
        new Thread(() -> userDao.updateForgetPassword().start();

        // Simuler l'envoi d'un email (dans une vraie application, vous utiliserez une API d'email)
        sendEmail(email, resetCode);

        return true;
    }*/



    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Générer un code à 6 chiffres
        return String.valueOf(code);
    }



    // Method to delete a user
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    // Method to update a user (can be called from an edit form)
    public void updateUser(User user) {
        // Call the DAO method with all the necessary fields from the User object
        userDao.updateUser(
                user.getUid(),
                user.getFirstName(),
                user.getLastName(),
                user.getTokenForgetPassword(),
                user.getResetTokenExpiration(),
                user.getPassword(),
                user.getEmail(),
                user.getGenre(),
                user.getAdresse(),
                user.getTelephone()
        );
    }


    // Changer le mot de passe après validation du code
    public boolean changePassword(String email, String newPassword) {

        List<User> users = userDao.getUserByEmail(email);


        if (users == null || users.isEmpty()) {
            return false; // L'utilisateur n'existe pas
        }

        User user = users.get(0);

        // Hasher le mdp
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        //update dooner user bel mdp jdid
        user.setPassword(hashedPassword);
        user.setTokenForgetPassword(null);
        user.setResetTokenExpiration(0); // Mettre à zéro l'expiration

        // Exécuter la mise à jour dans un thread séparé pour ne pas bloquer le thread principal
        new Thread(() -> userDao.updatePasswordAndResetToken(user.getPassword(), null, 0, user.getEmail())).start();

        return true;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

}
