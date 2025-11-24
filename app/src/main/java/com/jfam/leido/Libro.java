package com.jfam.leido;

import java.io.Serializable;
import java.util.UUID;

/**
 * Modelo de datos para representar un libro
 */
public class Libro implements Serializable {
    private String id;
    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;
    private String comentario;
    private boolean esLeido;
    private String urlPortada;
    private String imagenBase64;

    public Libro(String titulo, String autor, String editorial, String isbn, String comentario, boolean esLeido) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo != null ? titulo : "";
        this.autor = autor != null ? autor : "";
        this.editorial = editorial != null ? editorial : "";
        this.isbn = isbn != null ? isbn : "";
        this.comentario = comentario != null ? comentario : "";
        this.esLeido = esLeido;
        this.urlPortada = "";
        this.imagenBase64 = "";
    }

    // === GETTERS ===
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean esLeido() {
        return esLeido;
    }

    public String getUrlPortada() {
        return urlPortada != null ? urlPortada : "";
    }

    public String getImagenBase64() {
        return imagenBase64 != null ? imagenBase64 : "";
    }

    // === SETTERS ===
    public void setEsLeido(boolean esLeido) {
        this.esLeido = esLeido;
    }

    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada != null ? urlPortada : "";
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64 != null ? imagenBase64 : "";
    }

    /**
     * Verifica si el libro tiene alguna portada (URL o imagen local)
     */
    public boolean tienePortada() {
        return (urlPortada != null && !urlPortada.isEmpty()) ||
                (imagenBase64 != null && !imagenBase64.isEmpty());
    }

    /**
     * Representación para mostrar en la lista
     */
    public String toStringLista() {
        if (autor.isEmpty()) return titulo;
        return titulo + " — " + autor;
    }
}