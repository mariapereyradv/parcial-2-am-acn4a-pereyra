package com.jfam.leido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity de Login
 * Usuario demo: demo / 1234
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etContrasena;
    private Button btnIniciarSesion;
    private TextView txtRegistrarse;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("leido_prefs", MODE_PRIVATE);

        inicializarVistas();
        crearUsuarioDemo();
        configurarBotones();
    }

    /**
     * Inicializa las referencias de las vistas
     */
    private void inicializarVistas() {
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        txtRegistrarse = findViewById(R.id.txtRegistrarse);
    }

    /**
     * Crea el usuario demo para pruebas
     */
    private void crearUsuarioDemo() {
        if (!prefs.contains("demo_creado")) {
            prefs.edit()
                    .putString("usuario_demo", "demo")
                    .putString("contrasena_demo", "1234")
                    .putBoolean("demo_creado", true)
                    .apply();
        }
    }

    /**
     * Configura el comportamiento de los botones
     */
    private void configurarBotones() {
        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        txtRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Valida credenciales e inicia sesión
     */
    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();

        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.validation_empty_fields),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar con usuario demo
        String usuarioDemo = prefs.getString("usuario_demo", "");
        String contrasenaDemo = prefs.getString("contrasena_demo", "");

        if (email.equals(usuarioDemo) && contrasena.equals(contrasenaDemo)) {
            // Login exitoso
            prefs.edit().putBoolean("sesion_activa", true).apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Login fallido
            Toast.makeText(this, getString(R.string.validation_credentials_error),
                    Toast.LENGTH_LONG).show();
        }
    }
}