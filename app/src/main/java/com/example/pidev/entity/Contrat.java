package com.example.pidev.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contrat")
public class Contrat {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cin;
    private String type;
    private int num;
    private String datestart;
    private String dateend;
    private int valeur;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getDatestart() { return datestart; }
    public void setDatestart(String datestart) { this.datestart = datestart; }

    public String getDateend() { return dateend; }
    public void setDateend(String dateend) { this.dateend = dateend; }

    public int getValeur() { return valeur; }
    public void setValeur(int valeur) { this.valeur = valeur; }
}
