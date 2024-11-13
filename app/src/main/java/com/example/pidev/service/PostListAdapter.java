package com.example.pidev.service;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.crudapp.architecture.PostViewModel;
import com.example.pidev.ALLConstants.Constants;
import com.example.pidev.MainActivity.ShowPostActivity;
import com.example.pidev.Model.PostViewModel;
import com.example.pidev.R;
import com.example.pidev.entity.PostModel;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private List<PostModel> postData = new ArrayList<>();
    private Context context;
    private PostViewModel postViewModel;

    public void updatePostList(List<PostModel> postData) {
        this.postData.clear();
        this.postData.addAll(postData);

        notifyDataSetChanged();
    }

    public PostListAdapter(PostViewModel postViewModel) {
        this.postViewModel = postViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PostModel post = postData.get(holder.getAdapterPosition());
        holder.postTitle.setText(post.getTitle());
        holder.postDesc.setText(post.getDescription());
        holder.upvote.setText(String.valueOf(post.getUpvote()));
        holder.downvote.setText(String.valueOf(post.getDownvote()));

        holder.showPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowPostActivity.class);
            intent.putExtra(Constants.POST_ID, post.getId());
            intent.putExtra(Constants.POST_TITLE, post.getTitle());
            intent.putExtra(Constants.POST_DESC, post.getDescription());
            intent.putExtra(Constants.POST_AUTHOR, post.getAuthor());
            intent.putExtra(Constants.POST_DATE, post.getDate());
            intent.putExtra(Constants.POST_UPVOTE_COUNT, post.getUpvote());
            intent.putExtra(Constants.POST_DOWNVOTE_COUNT, post.getDownvote());
            context.startActivity(intent);
        });

        holder.upvoteBtn.setOnClickListener(view -> {
            post.setUpvote(post.getUpvote() + 1);
            postViewModel.update(post);
        });

        holder.downvoteBtn.setOnClickListener(view -> {
            post.setDownvote(post.getDownvote() + 1);
            postViewModel.update(post);
        });

    }

    @Override
    public int getItemCount() {
        return postData != null ? postData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postTitle, postDesc, upvote, downvote;
        public Button showPostButton;
        public ImageButton upvoteBtn, downvoteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.postTitle = itemView.findViewById(R.id.post_title);
            this.postDesc = itemView.findViewById(R.id.post_desc);
            this.showPostButton = itemView.findViewById(R.id.show_post_btn);
            this.upvote = itemView.findViewById(R.id.upvote_count);
            this.downvote = itemView.findViewById(R.id.downvote_count);
            this.upvoteBtn = itemView.findViewById(R.id.upvote_btn);
            this.downvoteBtn = itemView.findViewById(R.id.downvote_btn);
        }
    }

}
