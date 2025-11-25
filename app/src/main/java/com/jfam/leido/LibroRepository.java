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

        // Si no hay libros, se ven ejemplos
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
        // Usar URLs de HTTPS que funcion
        Libro libro1 = new Libro("Cien años de soledad", "Gabriel García Márquez",
                "Sudamericana", "978-0-307-47472-8",
                "Obra maestra del realismo mágico", true);
        libro1.setUrlPortada("https://m.media-amazon.com/images/I/81MI6+TpYyL._SY466_.jpg");
        libros.add(libro1);

        Libro libro2 = new Libro("1984", "George Orwell",
                "Secker & Warburg", "978-0-452-28423-4",
                "Distopía clásica", true);
        libro2.setUrlPortada("https://m.media-amazon.com/images/I/61ZewDE3beL._SY466_.jpg");
        libros.add(libro2);

        Libro libro3 = new Libro("El principito", "Antoine de Saint-Exupéry",
                "Reynal & Hitchcock", "978-0-156-01219-2",
                "Quiero releerlo", false);
        libro3.setUrlPortada("https://m.media-amazon.com/images/I/71OZY035FKL._SY466_.jpg");
        libros.add(libro3);

        Libro libro4 = new Libro("Harry Potter", "J.K. Rowling",
                "Bloomsbury", "978-0-439-70818-8",
                "Para leer pronto", false);
        libro4.setUrlPortada("https://m.media-amazon.com/images/I/81m1s4wIPML._SY466_.jpg");
        libros.add(libro4);

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