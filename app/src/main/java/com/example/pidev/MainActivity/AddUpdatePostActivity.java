package com.example.pidev.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pidev.Model.PostViewModel;
import com.example.pidev.ALLConstants.Constants;
import com.example.pidev.R;
import com.example.pidev.entity.PostModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUpdatePostActivity extends AppCompatActivity {

    EditText titleEditText, descEditText, authorEditText;
    Button submitPostBtn;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        // Get the ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Find the UI elements by their IDs
        titleEditText = findViewById(R.id.title_edit_text);
        descEditText = findViewById(R.id.desc_edit_text);
        authorEditText = findViewById(R.id.author_edit_text);
        submitPostBtn = findViewById(R.id.submit_post_btn);

        // Get the intent and the action type from it
        Intent intent = getIntent();
        String actionType = intent.getStringExtra(Constants.ACTION_TYPE);

        // If the action type is EDIT, populate the UI with the existing post details
        if (actionType.equals(Constants.ACTION_TYPE_EDIT)) {
            titleEditText.setText(intent.getStringExtra(Constants.POST_TITLE));
            descEditText.setText(intent.getStringExtra(Constants.POST_DESC));
            authorEditText.setText(intent.getStringExtra(Constants.POST_AUTHOR));
        }

        // When the submit button is clicked, get the input data, create a new post object, and save it to the database
        submitPostBtn.setOnClickListener(view -> {

            String title = titleEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();

            String curDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            PostModel post = new PostModel(title, desc, author, curDate);

            String result = "";

            // If the action type is ADD, insert the new post into the database
            if (actionType.equals(Constants.ACTION_TYPE_ADD)) {
                result = postViewModel.insert(post);
            }
            // If the action type is EDIT, update the existing post in the database
            else {
                int id = intent.getIntExtra(Constants.POST_ID, -1);
                post.setId(id);
                result = postViewModel.update(post);
            }

            // If the operation is successful, go back to the MainActivity, else show a Toast with the error message
            if (result.equals(Constants.SUCCESS)) {
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            } else {
                Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
