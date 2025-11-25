package com.jfam.leido;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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

        // Icono de perfil - cierra sesión
        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
}