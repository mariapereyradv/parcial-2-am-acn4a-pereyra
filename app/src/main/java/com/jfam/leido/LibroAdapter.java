package com.jfam.leido;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import androidx.core.content.ContextCompat;

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

            // LIMPIAR VISTA PRIMERO
            imgPortada.setImageDrawable(null);
            imgPortada.setBackgroundColor(Color.TRANSPARENT);

            // CARGAR IMAGEN
            String urlPortada = libro.getUrlPortada();
            String base64 = libro.getImagenBase64();

            boolean tieneImagen = false;

            if (base64 != null && !base64.trim().isEmpty()) {
                // Tiene imagen Base64
                try {
                    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            decodedString, 0, decodedString.length
                    );
                    if (bitmap != null) {
                        imgPortada.setImageBitmap(bitmap);
                        tieneImagen = true;
                    }
                } catch (Exception e) {
                    Log.e("LibroAdapter", "Error al decodificar Base64", e);
                }
            }

            if (!tieneImagen && urlPortada != null && !urlPortada.trim().isEmpty()) {
                // Tiene URL
                Glide.with(itemView.getContext())
                        .load(urlPortada)
                        .placeholder(android.R.color.transparent)
                        .error(R.color.primary_light)
                        .centerCrop()
                        .into(imgPortada);
                tieneImagen = true;
            }

            if (!tieneImagen) {
                // Sin imagen - mostrar color de fondo
                imgPortada.setBackgroundColor(
                        androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.primary_light)
                );
            }

            // Eventos (mantener igual)
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
}
}
