package com.jfam.leido;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etContrasena;
    private Button btnIniciarSesion;
    private TextView txtRegistrarse;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Verificar si ya hay sesión activa
        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if (usuarioActual != null) {
            // Ya hay sesión, ir directo a Main
            irAMain();
            return;
        }

        setContentView(R.layout.activity_login);
        inicializarVistas();
        configurarBotones();
    }

    private void inicializarVistas() {
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtRegistrarse = findViewById(R.id.txtRegistrarse);
    }

    private void configurarBotones() {
        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        txtRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();

        // Validaciones
        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.validation_empty_fields),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.validation_email_invalid));
            etEmail.requestFocus();
            return;
        }

        // Mostrar loading
        btnIniciarSesion.setEnabled(false);
        btnIniciarSesion.setText("Iniciando...");

        // Login con Firebase
        mAuth.signInWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(this, task -> {
                    btnIniciarSesion.setEnabled(true);
                    btnIniciarSesion.setText(getString(R.string.login_button));

                    if (task.isSuccessful()) {
                        // Login exitoso
                        Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                        irAMain();
                    } else {
                        // Error
                        String error = task.getException() != null ?
                                task.getException().getMessage() :
                                "Error desconocido";
                        Toast.makeText(this, "Error: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irAMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}