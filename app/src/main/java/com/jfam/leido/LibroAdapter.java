package com.jfam.leido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.bumptech.glide.Glide;
import android.widget.ImageView;

/**
 * Adaptador para mostrar la lista de libros en RecyclerView
 */
public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {

    private List<Libro> libros;
    private OnLibroListener listener;

    /**
     * Interfaz para manejar eventos sobre los libros
     */
    public interface OnLibroListener {
        void alMantenerPresionado(Libro libro);
        void alClickear(Libro libro);
    }

    public LibroAdapter(List<Libro> libros, OnLibroListener listener) {
        this.libros = libros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        return new LibroViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.vincular(libro, listener);
    }

    @Override
    public int getItemCount() {
        return libros != null ? libros.size() : 0;
    }

    /**
     * Actualiza la lista completa de libros
     */
    public void actualizarLibros(List<Libro> nuevosLibros) {
        this.libros = nuevosLibros;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder que contiene las vistas de cada item
     */
    static class LibroViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPortada;
        TextView txtTitulo;
        TextView txtAutor;
        TextView btnEditar;
        TextView btnEliminar;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.imgPortada);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        /**
         * Vincula los datos del libro con las vistas
         */
        public void vincular(Libro libro, OnLibroListener listener) {
            txtTitulo.setText(libro.getTitulo());
            String autorTexto = libro.getAutor().isEmpty() ?
                    itemView.getContext().getString(R.string.autor_desconocido) :
                    libro.getAutor();
            txtAutor.setText(autorTexto);

            // CARGAR IMAGEN: prioridad Base64, luego URL
            if (libro.getImagenBase64() != null && !libro.getImagenBase64().isEmpty()) {
                // Cargar desde Base64
                try {
                    byte[] decodedString = android.util.Base64.decode(
                            libro.getImagenBase64(), android.util.Base64.DEFAULT);
                    android.graphics.Bitmap bitmap = android.graphics.BitmapFactory
                            .decodeByteArray(decodedString, 0, decodedString.length);
                    imgPortada.setImageBitmap(bitmap);
                } catch (Exception e) {
                    imgPortada.setImageResource(android.R.color.transparent);
                    imgPortada.setBackgroundColor(itemView.getContext()
                            .getResources().getColor(R.color.primary_light));
                }
            } else if (libro.getUrlPortada() != null && !libro.getUrlPortada().isEmpty()) {
                // Cargar desde URL con Glide
                Glide.with(itemView.getContext())
                        .load(libro.getUrlPortada())
                        .placeholder(R.color.primary_light)
                        .error(R.color.primary_light)
                        .centerCrop()
                        .into(imgPortada);
            } else {
                // Sin portada
                imgPortada.setImageResource(android.R.color.transparent);
                imgPortada.setBackgroundColor(itemView.getContext()
                        .getResources().getColor(R.color.primary_light));
            }

            // Resto del código igual...
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.alClickear(libro);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.alMantenerPresionado(libro);
                }
                return true;
            });

            btnEditar.setOnClickListener(v -> {
                // TODO: Implementar edición
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.alMantenerPresionado(libro);
                }
            });
        }