package com.jfam.leido;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import java.io.Serializable;

public class Libro implements Serializable {

    @DocumentId
    private String id;

    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;
    private String comentario;
    private boolean esLeido;
    private String urlPortada;
    private String imagenBase64;
    private String userId;

    // Constructor vacío
    public Libro() {
        this.titulo = "";
        this.autor = "";
        this.editorial = "";
        this.isbn = "";
        this.comentario = "";
        this.urlPortada = "";
        this.imagenBase64 = "";
    }

    // Constructor con parámetros
    public Libro(String titulo, String autor, String editorial, String isbn,
                 String comentario, boolean esLeido) {
        this.titulo = titulo != null ? titulo : "";
        this.autor = autor != null ? autor : "";
        this.editorial = editorial != null ? editorial : "";
        this.isbn = isbn != null ? isbn : "";
        this.comentario = comentario != null ? comentario : "";
        this.esLeido = esLeido;
        this.urlPortada = "";
        this.imagenBase64 = "";
    }

    // === GETTERS  ===
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getIsbn() { return isbn; }
    public String getComentario() { return comentario; }
    public boolean isEsLeido() { return esLeido; }
    public String getUrlPortada() { return urlPortada != null ? urlPortada : ""; }
    public String getImagenBase64() { return imagenBase64 != null ? imagenBase64 : ""; }
    public String getUserId() { return userId; }

    // === SETTERS ===
    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setEsLeido(boolean esLeido) { this.esLeido = esLeido; }
    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada != null ? urlPortada : "";
    }
    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64 != null ? imagenBase64 : "";
    }
    public void setUserId(String userId) { this.userId = userId; }

    // === MÉTODOS AUXILIARES ===
    @Exclude
    public boolean esLeido() { return esLeido; }

    @Exclude
    public boolean tienePortada() {
        return (urlPortada != null && !urlPortada.isEmpty()) ||
                (imagenBase64 != null && !imagenBase64.isEmpty());
    }

    @Exclude
    public String toStringLista() {
        if (autor.isEmpty()) return titulo;
        return titulo + " – " + autor;
    }
}