package com.example.desarrolloproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        signupName = findViewById(R.id.singup_name);
        signupEmail = findViewById(R.id.singup_email);
        signupUsername = findViewById(R.id.singup_username);
        signupPassword = findViewById(R.id.singup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.singup_button);

        signupButton.setOnClickListener(v -> {

            String name = signupName.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            String rol;
            if (username.equals("admin")) {
                rol = "admin";
            } else {
                rol = "usuario";
            }

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("usuarios");

            String id = reference.push().getKey();

            HelperClass helperClass = new HelperClass(name, email, username, password, rol);

            reference.child(id).setValue(helperClass);

            Toast.makeText(SignupActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
