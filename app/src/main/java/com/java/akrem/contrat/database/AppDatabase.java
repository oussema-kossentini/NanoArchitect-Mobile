package com.java.akrem.contrat.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.java.akrem.contrat.Dao.ContratDao;
import com.java.akrem.contrat.entity.Contrat;

@Database(entities = {Contrat.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ContratDao contratDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "contrat_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
