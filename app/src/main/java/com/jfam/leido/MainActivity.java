package com.jfam.leido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
import android.widget.Toast;

/**
 * Activity Principal - Maneja la navegación entre fragmentos
 * Leídos y Deseados usando tabs
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FloatingActionButton fabAgregar;
    private TextView imgPerfil;
    private boolean enTabLeidos = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMPORAL: FORZAR RESET
        // SharedPreferences prefs = getSharedPreferences("libros_guardados", MODE_PRIVATE);
        // prefs.edit().clear().apply();
        //LibroRepository.obtenerInstancia(this).resetearDatos();

        inicializarVistas();
        configurarTabs();
        configurarBotones();

        cargarLibrosDesdeFirestore();

        // Mostrar fragmento de Leídos por defecto
        if (savedInstanceState == null) {
            cargarFragmento(new FragmentLeidos());
        }
    }

    /**
     * Inicializa las referencias de las vistas
     */
    private void inicializarVistas() {
        tabLayout = findViewById(R.id.tabLayout);
        fabAgregar = findViewById(R.id.fabAgregar);
        imgPerfil = findViewById(R.id.imgPerfil);
    }

    /**
     * Configura el comportamiento de los tabs
     */
    private void configurarTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragmento = null;
                if (tab.getPosition() == 0) {
                    // Tab Leídos
                    fragmento = new FragmentLeidos();
                    enTabLeidos = true;
                } else {
                    // Tab Deseados
                    fragmento = new FragmentDeseados();
                    enTabLeidos = false;
                }
                cargarFragmento(fragmento);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Configura los botones de la interfaz
     */
    private void configurarBotones() {
        // Botón flotante de agregar
        fabAgregar.setOnClickListener(v -> {
            DialogAgregarLibro dialogo = DialogAgregarLibro.nuevaInstancia(enTabLeidos);
            dialogo.show(getSupportFragmentManager(), "DialogAgregarLibro");
        });

        // Icono de perfil - cerrar sesión
        imgPerfil.setOnClickListener(v -> {
            // Obtener email del usuario
            com.google.firebase.auth.FirebaseUser user =
                    com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
            String email = user != null && user.getEmail() != null ? user.getEmail() : "usuario";

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Querés cerrar sesión de " + email + "?")
                    .setPositiveButton("Sí, cerrar sesión", (dialog, which) -> {
                        // Cerrar sesión
                        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();

                        // Ir al login
                        android.content.Intent intent = new android.content.Intent(
                                MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        android.widget.Toast.makeText(this,
                                "Sesión cerrada correctamente",
                                android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    /**
     * Carga un fragmento en el contenedor
     */
    private void cargarFragmento(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmento)
                .commit();
    }

    /**
     * Refresca el fragmento actualmente visible
     */
    public void refrescarFragmentoActual() {
        Fragment fragmentoActual = getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);

        if (fragmentoActual instanceof FragmentLeidos) {
            ((FragmentLeidos) fragmentoActual).refrescarLista();
        } else if (fragmentoActual instanceof FragmentDeseados) {
            ((FragmentDeseados) fragmentoActual).refrescarLista();
        }
    }

    /**
     * Carga los libros desde Firestore al iniciar
     */
    private void cargarLibrosDesdeFirestore() {
        LibroRepository.obtenerInstancia().cargarLibros(
                new LibroRepository.OnLibrosListener() {
                    @Override
                    public void onLibrosCargados(List<Libro> libros) {
                        // Solo refrescar, sin crear libros de ejemplo
                        refrescarFragmentoActual();
                    }

                    @Override
                    public void onError(String mensaje) {
                        Toast.makeText(MainActivity.this,
                                "Error al cargar libros: " + mensaje, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}