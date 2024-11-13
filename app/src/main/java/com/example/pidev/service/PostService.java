package com.example.pidev.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.PostModel;
import com.example.pidev.dao.PostDao;

import java.util.List;

public class PostService {

    private PostDao postDao;
    private LiveData<List<PostModel>> allPosts;

    public PostService(Context context) {
        AppDataBase db = AppDataBase.getAppDatabase(context);
        postDao = db.postDao();
        allPosts = postDao.getAll();
    }

    public LiveData<List<PostModel>> getAllPosts() {
        return allPosts;
    }

    public void insert(PostModel post){
        AppDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                postDao.insert(post);
            }
        });
    }

    public void update(PostModel post){
        AppDataBase.databaseWriteExecutor.execute(()->{
            postDao.update(post);
        });
    }

    public void delete(PostModel post){
        AppDataBase.databaseWriteExecutor.execute(()->{
            postDao.delete(post);
        });
    }


}
