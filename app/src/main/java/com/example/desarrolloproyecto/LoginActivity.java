package com.example.desarrolloproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView singupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        singupRedirectText = findViewById(R.id.singupRedirectText);

        loginButton.setOnClickListener(v -> {
            if (!validarNombre() || !validarContraseña()) {
                return;
            }
            validarUsuario();
        });

        singupRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    public Boolean validarNombre(){
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Nombre de usuario no puede estar vacío");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validarContraseña(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Contraseña no puede estar vacía");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void validarUsuario(){

        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");

        Query query = reference.orderByChild("username").equalTo(userUsername);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {

                            String rol = userSnapshot.child("rol").getValue(String.class);

                            if ("admin".equals(rol)) {
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                            return; // 🔴 importante para evitar múltiples accesos
                        }
                    }

                    loginPassword.setError("Contraseña incorrecta");

                } else {
                    loginUsername.setError("Usuario no existe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}