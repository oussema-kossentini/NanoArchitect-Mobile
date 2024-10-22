package com.example.pidevv1.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.Instant;
import java.util.Random;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.pidevv1.dao.UserDao;
import com.example.pidevv1.database.AppDataBase;
import com.example.pidevv1.entity.User;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {
    private UserDao userDao;
    private SharedPreferences mPreferences;
    private List<String> tokenBlacklist = new ArrayList<>(); // Liste des tokens blacklistés

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

    // Service pour l'enregistrement d'un utilisateur
    public boolean register(String firstName, String lastName, String email, String password) {
        List<User> existingUsers = userDao.getUserByEmail(email);
        if (existingUsers != null && !existingUsers.isEmpty()) {
            return false; // L'utilisateur existe déjà
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);

        new Thread(() -> userDao.insetOne(newUser)).start();
        String token = generateToken(newUser.getUid());

        // Stocker le token dans les SharedPreferences
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

            // Stocker les informations utilisateur dans les SharedPreferences
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("user_id", String.valueOf(user.getUid()));
            editor.putString("user_first_name", user.getFirstName());
            editor.putString("user_last_name", user.getLastName());
            editor.putString("user_email", user.getEmail());
            editor.putString("user_genre", user.getGenre());
            editor.putString("user_adresse", user.getAdresse());
            editor.putString("user_telephone", user.getTelephone());
            editor.apply();
        } else {
            Log.e("UserService", "User not found");
        }
    }
    // Méthode pour la connexion
    public boolean seConnecter(String email, String password) {
        List<User> users = userDao.getUserByEmail(email);
        if (users != null && users.size() == 1) {
            User user = users.get(0);

            // Vérifier que le mot de passe fourni correspond au mot de passe haché stocké
            if (BCrypt.checkpw(password, user.getPassword())) {
                String token = generateToken(user.getUid());

                // Stocker le token dans les SharedPreferences
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("jwt_token", token);
                editor.apply();
                getUserInfo(email);
                return true;  // Connexion réussie
            }
        }
        return false;  // Connexion échouée
    }

    // Service pour la déconnexion
    public void logout() {
        String token = mPreferences.getString("jwt_token", null);
        if (token != null) {
            tokenBlacklist.add(token); // Ajouter le token à la blacklist
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.remove("jwt_token"); // Supprimer le token des préférences partagées
            editor.apply();
        }
    }

    // Générer un JWT avec l'ID utilisateur
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

    // Vérifier si un token est blacklisté
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    public boolean verifyResetCode(String email, String code) {

        // Exécuter en tâche de fond
        List<User> users = userDao.getUserByEmail(email);

        if (users.isEmpty()) {
            return false; // L'utilisateur n'existe pas
        }

        User user = users.get(0); // On suppose qu'il n'y a qu'un seul utilisateur avec cet email
        long currentTimeMillis = System.currentTimeMillis(); // Obtenir l'heure actuelle en millisecondes

        // Récupérer le temps d'expiration stocké en millisecondes
        long expirationTimeMillis = user.getResetTokenExpiration();

        // Comparer les timestamps pour vérifier l'expiration
        boolean tokenNotExpired = currentTimeMillis < expirationTimeMillis;

        // Vérifier si le token correspond
        boolean tokenMatches = user.getTokenForgetPassword().equals(code);

        return tokenMatches && tokenNotExpired;
    }
    public boolean sendForgetPasswordCode(String email) {
        try {
            List<User> users = userDao.getUserByEmail(email);

            if (users == null || users.isEmpty()) {
                return false; // L'utilisateur n'existe pas
            }

            User user = users.get(0);
            String resetCode = generateResetCode();
            if (resetCode == null) {
                // Gérer l'erreur de génération du code
                return false;
            }

            // Calculer l'expiration du token en millisecondes (10 minutes à partir de maintenant)
            long expirationTimeMillis = System.currentTimeMillis() + (10 * 60 * 1000);

            user.setTokenForgetPassword(resetCode);
            user.setResetTokenExpiration(expirationTimeMillis);

            // Mettre à jour la base de données dans un thread séparé
            new Thread(() -> {
                try {
                    userDao.updateForgetPassword(user.getTokenForgetPassword(), expirationTimeMillis, email);
                } catch (Exception e) {
                    // Gérer l'exception de mise à jour
                    Log.e("Error", "Failed to update user", e);
                }
            }).start();

            // Simuler l'envoi d'un email (dans une vraie application, vous utiliserez une API d'email)
            sendEmail(email, resetCode);

            return true;
        } catch (Exception e) {
            // Gérer les exceptions générales
            Log.e("Error", "Failed to send forget password code", e);
            return false;
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


    // Générer un code de réinitialisation à 6 chiffres
    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Générer un code à 6 chiffres
        return String.valueOf(code);
    }

    // Simuler l'envoi d'un email
    private void sendEmail(String email, String resetCode) {
        // Simuler l'envoi de l'email (à remplacer par une vraie méthode d'envoi d'email)
      //  user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(10));
        System.out.println("Envoyer le code " + resetCode + " à l'adresse " + email);
    }



    // Changer le mot de passe après validation du code
    public boolean changePassword(String email, String newPassword) {
        // Récupérer l'utilisateur via l'email
        List<User> users = userDao.getUserByEmail(email);

        // Vérifier si l'utilisateur existe
        if (users == null || users.isEmpty()) {
            return false; // L'utilisateur n'existe pas
        }

        User user = users.get(0);

        // Hasher le nouveau mot de passe
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Mettre à jour le mot de passe, réinitialiser le token et l'expiration
        user.setPassword(hashedPassword);
        user.setTokenForgetPassword(null);
        user.setResetTokenExpiration(0); // Mettre à zéro l'expiration

        // Exécuter la mise à jour dans un thread séparé pour ne pas bloquer le thread principal
        new Thread(() -> userDao.updatePasswordAndResetToken(user.getPassword(), null, 0, user.getEmail())).start();

        return true; // Mot de passe changé avec succès
    }

}
