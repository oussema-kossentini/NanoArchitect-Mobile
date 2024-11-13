package com.example.pidev.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class PostModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title, description, author, date;
    private int upvote, downvote;


    public PostModel(String title, String description, String author, String date, int upvote, int downvote) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    @Ignore
    public PostModel(String title, String description, String author, String date) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}

