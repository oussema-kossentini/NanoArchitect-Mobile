package com.example.pidev.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pidev.ALLConstants.Constants;
import com.example.pidev.Model.PostViewModel;
import com.example.pidev.R;
import com.example.pidev.entity.PostModel;


public class ShowPostActivity extends AppCompatActivity {

    TextView postTitleView, postDescView, postAuthorView, postDateView, upvoteCountView, downvoteCountView;
    Button deleteBtn, editBtn;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        postTitleView = findViewById(R.id.title_text_view);
        postDescView = findViewById(R.id.desc_text_view);
        postAuthorView = findViewById(R.id.author_text_view);
        postDateView = findViewById(R.id.date_text_view);
        upvoteCountView = findViewById(R.id.upvote_count_view);
        downvoteCountView = findViewById(R.id.downvote_count_view);
        deleteBtn = findViewById(R.id.delete_btn);
        editBtn = findViewById(R.id.edit_btn);
        postDescView.setMovementMethod(new ScrollingMovementMethod());
        postTitleView.setMovementMethod(new ScrollingMovementMethod());

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.POST_TITLE);
        String desc = intent.getStringExtra(Constants.POST_DESC);
        String author = intent.getStringExtra(Constants.POST_AUTHOR);
        String date = intent.getStringExtra(Constants.POST_DATE);
        int upvotes = intent.getIntExtra(Constants.POST_UPVOTE_COUNT, 0);
        int downvotes = intent.getIntExtra(Constants.POST_DOWNVOTE_COUNT, 0);

        postTitleView.setText(title);
        postDescView.setText(desc);
        postAuthorView.setText(author);
        postDateView.setText(date);
        upvoteCountView.setText(String.valueOf(upvotes));
        downvoteCountView.setText(String.valueOf(downvotes));

        int id = intent.getIntExtra("POST_ID", -1);

        editBtn.setOnClickListener(view -> {

            Intent editIntent = new Intent(this, AddUpdatePostActivity.class);
            editIntent.putExtra(Constants.ACTION_TYPE, Constants.ACTION_TYPE_EDIT);
            editIntent.putExtra(Constants.POST_ID, id);
            editIntent.putExtra(Constants.POST_TITLE, title);
            editIntent.putExtra(Constants.POST_DESC, desc);
            editIntent.putExtra(Constants.POST_AUTHOR, author);
            editIntent.putExtra(Constants.POST_DATE, date);
            editIntent.putExtra(Constants.POST_UPVOTE_COUNT, upvotes);
            editIntent.putExtra(Constants.POST_DOWNVOTE_COUNT, downvotes);

            startActivity(editIntent);
            this.finish();

        });

        deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmer la suppression");
            builder.setMessage("Etes-vous sûr de vouloir supprimer ce blog ?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                PostModel post = new PostModel(title, desc, author, date, upvotes, downvotes);
                post.setId(id);
                postViewModel.delete(post);
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            });
            builder.setNegativeButton("Annuler", (dialog, which) -> {
                // do nothing
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });

    }
}