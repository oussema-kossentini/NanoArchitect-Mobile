package com.example.pidev.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pidev.dao.ContratDao;
import com.example.pidev.dao.UserDao;
import com.example.pidev.entity.Contrat;
import com.example.pidev.entity.User;
@Database(entities = {User.class, Contrat.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;

    public abstract UserDao userDao();
    public abstract ContratDao contratDao();

    public static AppDataBase getAppDatabase(Context context) {

        if (instance == null) {
            synchronized (AppDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "room_test_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}
/*
package com.java.akrem.contrat;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

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

 */
