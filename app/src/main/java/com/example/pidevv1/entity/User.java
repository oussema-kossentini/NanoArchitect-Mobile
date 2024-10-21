package com.example.pidevv1.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "TokenForgetPassword")
    private String TokenForgetPassword;

  /*  public LocalDateTime getResetTokenExpiration() {
        return ResetTokenExpiration;
    }

    public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
        ResetTokenExpiration = resetTokenExpiration;
    }

    @ColumnInfo(name = "ResetTokenExpiration")
    LocalDateTime ResetTokenExpiration;

    */
   @ColumnInfo(name = "ResetTokenExpiration")
   private long resetTokenExpiration;


    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "Email")
    private String Email;

    public String getTokenForgetPassword()
    {return TokenForgetPassword;}

    public void setTokenForgetPassword(String TokenForgetPassword) {
        this.TokenForgetPassword = TokenForgetPassword;
    }
    public long getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public void setResetTokenExpiration(long resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
