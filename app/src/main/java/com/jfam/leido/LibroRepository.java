package com.jfam.leido;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class LibroRepository {
    private static final String TAG = "LibroRepository";
    private static LibroRepository instancia;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Libro> librosCache;

    private LibroRepository() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        librosCache = new ArrayList<>();
    }

    public static synchronized LibroRepository obtenerInstancia() {
        if (instancia == null) {
            instancia = new LibroRepository();
        }
        return instancia;
    }

    private String getUserId() {
        return mAuth.getCurrentUser() != null ?
                mAuth.getCurrentUser().getUid() : "";
    }

    public void cargarLibros(OnLibrosListener listener) {
        String userId = getUserId();
        if (userId.isEmpty()) {
            listener.onError("Usuario no autenticado");
            return;
        }

        db.collection("usuarios")
                .document(userId)
                .collection("libros")
                .orderBy("titulo", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    librosCache.clear();
                    librosCache.addAll(
                            queryDocumentSnapshots.toObjects(Libro.class)
                    );
                    Log.d(TAG, "Libros cargados: " + librosCache.size());
                    listener.onLibrosCargados(librosCache);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar libros", e);
                    listener.onError(e.getMessage());
                });
    }

    public List<Libro> obtenerLeidos() {
        List<Libro> leidos = new ArrayList<>();
        for (Libro libro : librosCache) {
            if (libro.isEsLeido()) {
                leidos.add(libro);
            }
        }
        return leidos;
    }

    public List<Libro> obtenerDeseados() {
        List<Libro> deseados = new ArrayList<>();
        for (Libro libro : librosCache) {
            if (!libro.isEsLeido()) {
                deseados.add(libro);
            }
        }
        return deseados;
    }

    public void agregarLibro(Libro libro, OnOperacionListener listener) {
        String userId = getUserId();
        if (userId.isEmpty()) {
            listener.onError("Usuario no autenticado");
            return;
        }

        libro.setUserId(userId);

        db.collection("usuarios")
                .document(userId)
                .collection("libros")
                .add(libro)
                .addOnSuccessListener(documentReference -> {
                    libro.setId(documentReference.getId());
                    librosCache.add(0, libro);
                    Log.d(TAG, "Libro agregado: " + libro.getTitulo());
                    listener.onExito("Libro agregado");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al agregar libro", e);
                    listener.onError(e.getMessage());
                });
    }

    public void eliminarLibro(Libro libro, OnOperacionListener listener) {
        String userId = getUserId();
        if (userId.isEmpty() || libro.getId() == null) {
            listener.onError("Error al eliminar");
            return;
        }

        db.collection("usuarios")
                .document(userId)
                .collection("libros")
                .document(libro.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    librosCache.remove(libro);
                    Log.d(TAG, "Libro eliminado: " + libro.getTitulo());
                    listener.onExito("Libro eliminado");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar libro", e);
                    listener.onError(e.getMessage());
                });
    }

    public void cambiarEstado(Libro libro, OnOperacionListener listener) {
        String userId = getUserId();
        if (userId.isEmpty() || libro.getId() == null) {
            listener.onError("Error al actualizar");
            return;
        }

        libro.setEsLeido(!libro.isEsLeido());

        db.collection("usuarios")
                .document(userId)
                .collection("libros")
                .document(libro.getId())
                .update("esLeido", libro.isEsLeido())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Estado cambiado: " + libro.getTitulo());
                    listener.onExito("Estado actualizado");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cambiar estado", e);
                    libro.setEsLeido(!libro.isEsLeido());
                    listener.onError(e.getMessage());
                });
    }

    public void crearLibrosEjemplo(OnOperacionListener listener) {
        if (!librosCache.isEmpty()) {
            listener.onExito("Ya hay libros");
            return;
        }

        String userId = getUserId();
        if (userId.isEmpty()) {
            listener.onError("Usuario no autenticado");
            return;
        }

        List<Libro> ejemplos = new ArrayList<>();

        Libro libro1 = new Libro("Cien años de soledad", "Gabriel García Márquez",
                "Sudamericana", "978-0-307-47472-8",
                "Obra maestra del realismo mágico", true);
        libro1.setUrlPortada("https://m.media-amazon.com/images/I/81MI6+TpYyL._SY466_.jpg");
        libro1.setUserId(userId);
        ejemplos.add(libro1);

        Libro libro2 = new Libro("1984", "George Orwell",
                "Secker & Warburg", "978-0-452-28423-4",
                "Distopía clásica sobre el totalitarismo", true);
        libro2.setUrlPortada("https://m.media-amazon.com/images/I/61ZewDE3beL._SY466_.jpg");
        libro2.setUserId(userId);
        ejemplos.add(libro2);

        for (Libro libro : ejemplos) {
            db.collection("usuarios")
                    .document(userId)
                    .collection("libros")
                    .add(libro);
        }

        librosCache.addAll(ejemplos);
        listener.onExito("Libros de ejemplo creados");
    }

    public interface OnLibrosListener {
        void onLibrosCargados(List<Libro> libros);
        void onError(String mensaje);
    }

    public interface OnOperacionListener {
        void onExito(String mensaje);
        void onError(String mensaje);
    }
}