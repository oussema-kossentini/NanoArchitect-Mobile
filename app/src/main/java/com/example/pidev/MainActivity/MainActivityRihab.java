package com.example.pidev.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

//import com.example.crudapp.architecture.PostViewModel;
import com.example.pidev.ALLConstants.Constants;
import com.example.pidev.Model.PostViewModel;
import com.example.pidev.R;
import com.example.pidev.entity.PostModel;
import com.example.pidev.service.PostListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainActivityRihab extends AppCompatActivity {


    private SharedPreferences mPreferences;

    private PostViewModel postViewModel;
    private FloatingActionButton addPostBtn, searchBtn;
    private EditText searchInput;
    private PostListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Création de la clé maître (MasterKey) en utilisant MasterKey.Builder
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Initialisation de EncryptedSharedPreferences
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    this,// Le contexte
                    "secret_shared_prefs",  // Nom du fichier SharedPreferences
                    masterKey,               // L'objet MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,   // Schéma pour les clés
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM  // Schéma pour les valeurs
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("login", "user123");
            editor.apply();
            mPreferences = sharedPreferences;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // Check if user is logged in
        //   SharedPreferences sharedPreferences = getSharedPreferences("com.example.pidevv1", MODE_PRIVATE);
        // Vérification de la présence du jeton JWT dans EncryptedSharedPreferences
      /*  String jwtToken = mPreferences.getString("jwt_token", null);
        if (jwtToken == null) {
            // Si aucun jeton n'est trouvé, rediriger vers l'activité Login
            Intent intent = new Intent(MainActivityRihab.this, Login.class);
            startActivity(intent);
            finish(); // Fermer MainActivity pour éviter qu'elle reste dans la pile
            return; // Sortie anticipée de onCreate car l'utilisateur est redirigé
        }
        else{Intent intent = new Intent(MainActivityRihab.this, home.class);
            startActivity(intent);}*/

        setContentView(R.layout.activity_mainnnnrihab);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        addPostBtn = findViewById(R.id.fab_add_post_btn);
        searchBtn = findViewById(R.id.fab_search);
        searchInput = findViewById(R.id.search_input);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        adapter = new PostListAdapter(postViewModel);

        postViewModel.getAllPosts().observe(this, posts -> adapter.updatePostList(posts));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Toggle visibility of search input when search button is clicked
        searchBtn.setOnClickListener(view -> {
            if (searchInput.getVisibility() == View.GONE) {
                searchInput.setVisibility(View.VISIBLE);
            } else {
                searchInput.setVisibility(View.GONE);
                adapter.updatePostList(postViewModel.getAllPosts().getValue());
            }
        });

        // Filter posts when text changes in search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterPosts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        addPostBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivityRihab.this, AddUpdatePostActivity.class);
            intent.putExtra(Constants.ACTION_TYPE, Constants.ACTION_TYPE_ADD);
            startActivity(intent);
        });
    }

    private void filterPosts(String query) {
        List<PostModel> filteredList = new ArrayList<>();
        if (postViewModel.getAllPosts().getValue() != null) {
            for (PostModel post : postViewModel.getAllPosts().getValue()) {
                if (post.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(post);
                }
            }
        }
        adapter.updatePostList(filteredList);
    }
}
