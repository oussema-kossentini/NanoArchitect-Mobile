package com.example.pidev.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pidev.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insetOne(User user);
   /* @Query("SELECT * FROM user_table")
    List<User> getAllUsers();*/
    @Delete
    void delete(User user);

    @Query("UPDATE user_table SET first_name = :firstName, last_name = :lastName, TokenForgetPassword = :tokenForgetPassword, " +
            "ResetTokenExpiration = :resetTokenExpiration, password = :password, Email = :email, genre = :genre, adresse = :adresse, telephone = :telephone " +
            "WHERE uid = :uid")
    void updateUser(int uid, String firstName, String lastName, String tokenForgetPassword, long resetTokenExpiration, String password,
                    String email, String genre, String adresse, String telephone);




    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    List<User> getUserByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM user_table WHERE email = :email ")
    List<User> getUserByEmail(String email);
    @Query("UPDATE user_table SET password = :newPassword, TokenForgetPassword = :newToken, ResetTokenExpiration = :expirationTime WHERE Email = :email")
    void updatePasswordAndResetToken(String newPassword, String newToken, long expirationTime, String email);
    @Query("UPDATE user_table SET TokenForgetPassword = :newToken, ResetTokenExpiration = :expirationTime WHERE email = :email")
    void updateForgetPassword(String newToken,long expirationTime, String email);
}

