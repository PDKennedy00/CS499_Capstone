package com.example.a5_3_project_two_by_dawson_kennedy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNewUser, editTextNewPass;
    private Button registerButton;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextNewUser = findViewById(R.id.editTextNewUser);
        editTextNewPass = findViewById(R.id.editTextNewpass);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = editTextNewUser.getText().toString().trim();
        String password = editTextNewPass.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long id = db.insert("users", null, values);
        if (id > 0) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish(); // Go back to login screen
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}
