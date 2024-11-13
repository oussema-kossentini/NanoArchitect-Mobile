package com.example.pidev.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.pidev.dao.ContratDao;
import com.example.pidev.dao.PostDao;
import com.example.pidev.dao.UserDao;
import com.example.pidev.entity.Contrat;
import com.example.pidev.entity.PostModel;
import com.example.pidev.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Contrat.class, PostModel.class}, version = 3, exportSchema = true)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;
  //  private static PostDatabase instance;
    public abstract UserDao userDao();
    public abstract ContratDao contratDao();
    public abstract PostDao postDao();
    public static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE user_table ADD COLUMN Role TEXT NOT NULL DEFAULT 'user'");

        }
    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Création de la table pour la nouvelle entité PostModel
            database.execSQL("CREATE TABLE IF NOT EXISTS `post_table` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`description` TEXT, " +
                    "`author` TEXT, " +
                    "`date` TEXT, " +
                    "`upvote` INTEGER NOT NULL, " +
                    "`downvote` INTEGER NOT NULL)");
        }
    };

    public static AppDataBase getAppDatabase(Context context) {

        if (instance == null) {
            synchronized (AppDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "room_test_db")
                            .allowMainThreadQueries()
                           // .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
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
