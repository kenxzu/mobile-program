package com.myfirstapp.lct1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etEmail, etAge;
    private Button btnAdd, btnView, btnUpdate, btnDelete;
    private TextView tvUsers;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        initViews();

        // Set click listeners
        setClickListeners();

        // Load and display all users
        displayAllUsers();
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etAge = findViewById(R.id.et_age);
        btnAdd = findViewById(R.id.btn_add);
        btnView = findViewById(R.id.btn_view);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        tvUsers = findViewById(R.id.tv_users);
    }

    private void setClickListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllUsers();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    private void addUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);

            // Insert user into database
            long id = db.insertUser(name, email, age);

            if (id != -1) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                clearFields();
                displayAllUsers();
                Log.d(TAG, "User inserted with ID: " + id);
            } else {
                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid age", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayAllUsers() {
        List<User> users = db.getAllUsers();

        if (users.isEmpty()) {
            tvUsers.setText("No users found");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total Users: ").append(users.size()).append("\n\n");

        for (User user : users) {
            sb.append("ID: ").append(user.getId()).append("\n");
            sb.append("Name: ").append(user.getName()).append("\n");
            sb.append("Email: ").append(user.getEmail()).append("\n");
            sb.append("Age: ").append(user.getAge()).append("\n");
            sb.append("------------------------\n");
        }

        tvUsers.setText(sb.toString());
        Log.d(TAG, "Displayed " + users.size() + " users");
    }

    private void updateUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // For this example, we'll update the first user
        List<User> users = db.getAllUsers();
        if (users.isEmpty()) {
            Toast.makeText(this, "No users to update", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            User user = users.get(0); // Update first user
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            int result = db.updateUser(user);

            if (result > 0) {
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                clearFields();
                displayAllUsers();
                Log.d(TAG, "User updated: " + user.toString());
            } else {
                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid age", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser() {
        List<User> users = db.getAllUsers();
        if (users.isEmpty()) {
            Toast.makeText(this, "No users to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete the first user for this example
        User user = users.get(0);
        db.deleteUser(user);

        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
        displayAllUsers();
        Log.d(TAG, "User deleted: " + user.toString());
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etAge.setText("");
    }
}