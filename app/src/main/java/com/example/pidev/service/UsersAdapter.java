package com.example.pidev.service;

//import static android.os.Build.VERSION_CODES.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.pidevv1.R;


import com.example.pidev.R;
import com.example.pidev.entity.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserActionListener listener;
    public UsersAdapter(List<User> userList,OnUserActionListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }
    public interface OnUserActionListener {
        void onEdit(User user);
        void onDelete(User user);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Binding the data to TextViews
        holder.textViewId.setText(String.valueOf(user.getUid()));
        holder.textViewName.setText(user.getFirstName() + " " + user.getLastName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewGenre.setText(user.getGenre());
        holder.textViewAddress.setText(user.getAdresse());
        holder.textViewPhone.setText(user.getTelephone());
        // Handle Edit Button click
        holder.btnUpdate.setOnClickListener(v -> {
            openUpdateDialog(holder.itemView.getContext(), user);
        });

        // Handle Delete Button click
        holder.btnDelete.setOnClickListener(v -> {
            listener.onDelete(user);
        });

    }
    private void openUpdateDialog(Context context, User user) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_user, null);
        builder.setView(dialogView);

        // Initialize dialog views
        EditText editTextFirstName = dialogView.findViewById(R.id.editTextFirstName);
        EditText editTextLastName = dialogView.findViewById(R.id.editTextLastName);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        Button btnDialogUpdate = dialogView.findViewById(R.id.btnDialogUpdate);

        // Set current user info into the EditTexts
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        editTextEmail.setText(user.getEmail());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle the Update button inside the dialog
        btnDialogUpdate.setOnClickListener(v -> {
            // Update the user object with the new data
            user.setFirstName(editTextFirstName.getText().toString());
            user.setLastName(editTextLastName.getText().toString());
            user.setEmail(editTextEmail.getText().toString());

            // Call the update method (from service, or wherever needed)
            listener.onEdit(user); // This should trigger the update in your ViewModel or database

            // Dismiss the dialog
            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewName, textViewEmail, textViewGenre, textViewAddress, textViewPhone;
        Button btnUpdate, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the TextViews
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewGenre = itemView.findViewById(R.id.textViewGenre);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
