package com.java.akrem.contrat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.java.akrem.contrat.entity.Contrat;

import java.util.List;

@Dao
public interface ContratDao {
    @Insert
    void insertContrat(Contrat contrat);

    @Update
    void updateContrat(Contrat contrat);

    @Delete
    void deleteContrat(Contrat contrat);

    @Query("SELECT * FROM contrat")
    List<Contrat> getAllContrats();  // Corrected to use com.java.akrem.contrat.entity.Contrat

    // This method deletes all rows from the "contrat" table
    @Query("DELETE FROM contrat")
    void deleteAllContrats();
}
