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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombreUsuario;
    private EditText etEmail;
    private EditText etContrasena;
    private EditText etConfirmarContrasena;
    private Button btnRegistrarse;
    private TextView txtIniciarSesion;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();

        inicializarVistas();
        configurarBotones();
    }

    private void inicializarVistas() {
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        txtIniciarSesion = findViewById(R.id.txtIniciarSesion);
    }

    private void configurarBotones() {
        btnRegistrarse.setOnClickListener(v -> registrarUsuario());

        txtIniciarSesion.setOnClickListener(v -> {
            finish(); // Volver al login
        });
    }

    private void registrarUsuario() {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();
        String confirmarContrasena = etConfirmarContrasena.getText().toString();

        // Validaciones
        if (nombreUsuario.isEmpty() || email.isEmpty() ||
                contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.validation_empty_fields),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.validation_email_invalid));
            etEmail.requestFocus();
            return;
        }

        if (contrasena.length() < 6) {
            etContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            etContrasena.requestFocus();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            etConfirmarContrasena.setError(getString(R.string.validation_passwords_mismatch));
            etConfirmarContrasena.requestFocus();
            return;
        }

        // Mostrar loading
        btnRegistrarse.setEnabled(false);
        btnRegistrarse.setText("Registrando...");

        // Registro con Firebase
        mAuth.createUserWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Actualizar nombre de usuario
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nombreUsuario)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        Toast.makeText(this,
                                                getString(R.string.registro_exitoso),
                                                Toast.LENGTH_SHORT).show();
                                        finish(); // Volver al login
                                    });
                        }
                    } else {
                        // Error
                        btnRegistrarse.setEnabled(true);
                        btnRegistrarse.setText(getString(R.string.register_button));

                        String error = task.getException() != null ?
                                task.getException().getMessage() :
                                "Error desconocido";
                        Toast.makeText(this, "Error: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}