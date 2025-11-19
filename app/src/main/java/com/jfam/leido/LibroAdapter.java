package com.jfam.leido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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

        TextView imgPortada;
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
            txtAutor.setText(libro.getAutor().isEmpty() ? "Autor desconocido" : libro.getAutor());

            // Click normal: ver detalle (segunda entrega)
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.alClickear(libro);
                }
            });

            // Long press: mostrar opciones
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.alMantenerPresionado(libro);
                }
                return true;
            });

            // Botón editar (segunda entrega)
            btnEditar.setOnClickListener(v -> {
                // TODO: Implementar edición en segunda entrega
            });

            // Botón eliminar
            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.alMantenerPresionado(libro);
                }
            });
        }
    }
}