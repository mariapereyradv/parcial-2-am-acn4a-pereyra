package com.jfam.leido;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio singleton para gestionar los libros
 * Usa SharedPreferences para persistencia
 */
public class LibroRepository {
    private static LibroRepository instancia;
    private List<Libro> libros;
    private SharedPreferences prefs;
    private Gson gson;
    private static final String PREF_LIBROS = "libros_guardados";
    private static final String KEY_LIBROS = "lista_libros";

    private LibroRepository(Context context) {
        prefs = context.getSharedPreferences(PREF_LIBROS, Context.MODE_PRIVATE);
        gson = new Gson();
        cargarLibros();

        // Si no hay libros, agregar ejemplos
        if (libros.isEmpty()) {
            agregarLibrosEjemplo();
        }
    }

    public static synchronized LibroRepository obtenerInstancia(Context context) {
        if (instancia == null) {
            instancia = new LibroRepository(context.getApplicationContext());
        }
        return instancia;
    }

    /**
     * Carga los libros desde SharedPreferences
     */
    private void cargarLibros() {
        String json = prefs.getString(KEY_LIBROS, null);
        if (json != null) {
            Type tipo = new TypeToken<ArrayList<Libro>>(){}.getType();
            libros = gson.fromJson(json, tipo);
        } else {
            libros = new ArrayList<>();
        }
    }

    /**
     * Guarda los libros en SharedPreferences
     */
    private void guardarLibros() {
        String json = gson.toJson(libros);
        prefs.edit().putString(KEY_LIBROS, json).apply();
    }

    /**
     * Agrega libros de ejemplo para pruebas
     */
    private void agregarLibrosEjemplo() {
        libros.add(new Libro("Cien años de soledad", "Gabriel García Márquez",
                "Sudamericana", "978-0-307-47472-8",
                "Obra maestra del realismo mágico", true));
        libros.add(new Libro("El nombre de la rosa", "Umberto Eco",
                "Sudamericana", "978-0-15-601219-2",
                "Misterio medieval fascinante", true));
        libros.add(new Libro("Rayuela", "Julio Cortázar",
                "Sudamericana", "978-0-307-47423-0",
                "Quiero leerlo pronto", false));
        libros.add(new Libro("Ficciones", "Jorge Luis Borges",
                "Emecé", "978-0-8021-3545-0", "", false));
        guardarLibros();
    }

    /**
     * Obtiene todos los libros leídos
     */
    public List<Libro> obtenerLeidos() {
        List<Libro> leidos = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.esLeido()) {
                leidos.add(libro);
            }
        }
        return leidos;
    }

    /**
     * Obtiene todos los libros deseados
     */
    public List<Libro> obtenerDeseados() {
        List<Libro> deseados = new ArrayList<>();
        for (Libro libro : libros) {
            if (!libro.esLeido()) {
                deseados.add(libro);
            }
        }
        return deseados;
    }

    /**
     * Agrega un nuevo libro al inicio de la lista
     */
    public void agregarLibro(Libro libro) {
        libros.add(0, libro);
        guardarLibros();
    }

    /**
     * Elimina un libro
     */
    public void eliminarLibro(Libro libro) {
        libros.remove(libro);
        guardarLibros();
    }

    /**
     * Cambia el estado de un libro (leído <-> deseado)
     */
    public void cambiarEstado(Libro libro) {
        libro.setEsLeido(!libro.esLeido());
        guardarLibros();
    }
}