package com.example.pidevv1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pidevv1.dao.UserDao;
import com.example.pidevv1.entity.User;
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;

    public abstract UserDao userDao();

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
