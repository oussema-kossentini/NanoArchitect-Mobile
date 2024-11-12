package com.example.pidev.MainActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pidev.database.AppDataBase;
import com.example.pidev.entity.User;
import com.example.pidev.service.UserService;
import com.example.pidev.service.UsersAdapter;
import com.example.pidev.R;

import java.util.List;

public class UserList extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;
    private AppDataBase appDataBase;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userService = new UserService(getApplication());

        // Fetch all users from the database
        new Thread(() -> {
            List<User> userList = userService.getAllUsers();

            // Update UI on the main thread
            runOnUiThread(() -> {
                usersAdapter = new UsersAdapter(userList, new UsersAdapter.OnUserActionListener() {
                    @Override
                    public void onEdit(User user) {
                        // This is where you update the user in the database
                        // Call your service method to update the user
                        userService.updateUser(user);

                        // Refresh the list or do whatever you need after the update
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDelete(User user) {
                        // Handle delete logic here
                        new Thread(() -> {
                            userService.deleteUser(user); // Call method to delete user from DB
                            runOnUiThread(() -> {
                                userList.remove(user); // Update the list in the UI
                                usersAdapter.notifyDataSetChanged(); // Refresh the list
                                Toast.makeText(UserList.this, "Deleted " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                            });
                        }).start();
                    }
                });
                recyclerViewUsers.setAdapter(usersAdapter);
            });
        }).start();
    }
}
