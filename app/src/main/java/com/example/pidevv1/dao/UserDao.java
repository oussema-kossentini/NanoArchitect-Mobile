package com.example.pidevv1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pidevv1.entity.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insetOne(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table")
    List<User> getAll();

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    List<User> getUserByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM user_table WHERE email = :email ")
    List<User> getUserByEmail(String email);
    @Query("UPDATE user_table SET password = :newPassword, TokenForgetPassword = :newToken, ResetTokenExpiration = :expirationTime WHERE Email = :email")
    void updatePasswordAndResetToken(String newPassword, String newToken, long expirationTime, String email);
    @Query("UPDATE user_table SET TokenForgetPassword = :newToken, ResetTokenExpiration = :expirationTime WHERE email = :email")
    void updateForgetPassword(String newToken,long expirationTime, String email);
}

