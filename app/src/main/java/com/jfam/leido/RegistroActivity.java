package com.jfam.leido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity de Registro
 * Permite crear una nueva cuenta
 */
public class RegistroActivity extends AppCompatActivity {

    private EditText etNombreUsuario;
    private EditText etEmail;
    private EditText etContrasena;
    private EditText etConfirmarContrasena;
    private CheckBox checkPoliticas;
    private Button btnRegistrarse;
    private TextView txtIniciarSesion;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        prefs = getSharedPreferences("leido_prefs", MODE_PRIVATE);

        inicializarVistas();
        configurarBotones();
    }

    /**
     * Inicializa las referencias de las vistas
     */
    private void inicializarVistas() {
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etEmail = findViewById(R.id.etEmail);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        checkPoliticas = findViewById(R.id.checkPoliticas);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        txtIniciarSesion = findViewById(R.id.txtIniciarSesion);
    }

    /**
     * Configura el comportamiento de los botones
     */
    private void configurarBotones() {
        btnRegistrarse.setOnClickListener(v -> registrarUsuario());

        txtIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Valida los datos y registra al usuario
     */
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

        if (contrasena.length() < 4) {
            etContrasena.setError(getString(R.string.validation_password_short));
            etContrasena.requestFocus();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            etConfirmarContrasena.setError(getString(R.string.validation_passwords_mismatch));
            etConfirmarContrasena.requestFocus();
            return;
        }

        if (!checkPoliticas.isChecked()) {
            Toast.makeText(this, getString(R.string.validation_accept_privacy),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar usuario (sobrescribe el demo)
        prefs.edit()
                .putString("usuario_demo", nombreUsuario)
                .putString("contrasena_demo", contrasena)
                .putString("email_usuario", email)
                .apply();

        Toast.makeText(this, getString(R.string.registro_exitoso), Toast.LENGTH_SHORT).show();

        // Ir al login
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}