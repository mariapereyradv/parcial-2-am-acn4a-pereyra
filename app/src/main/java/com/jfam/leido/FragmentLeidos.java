package com.jfam.leido;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Fragmento que muestra la lista de libros leídos
 *
 */
public class FragmentLeidos extends Fragment {

    private RecyclerView recyclerView;
    private LibroAdapter adapter;
    private LibroRepository repositorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_leidos, container, false);

        repositorio = LibroRepository.obtenerInstancia();
        recyclerView = vista.findViewById(R.id.recyclerLeidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        configurarAdapter();
        refrescarLista();

        return vista;
    }

    /**
     * Configura el adaptador con los listeners
     */
    private void configurarAdapter() {
        adapter = new LibroAdapter(repositorio.obtenerLeidos(), new LibroAdapter.OnLibroListener() {
            @Override
            public void alMantenerPresionado(Libro libro) {
                mostrarDialogoEliminar(libro);
            }

            @Override
            public void alClickear(Libro libro) {
                Intent intent = new Intent(requireContext(), DetalleLibroActivity.class);
                intent.putExtra("titulo", libro.getTitulo());
                intent.putExtra("autor", libro.getAutor());
                intent.putExtra("editorial", libro.getEditorial());
                intent.putExtra("isbn", libro.getIsbn());
                intent.putExtra("comentario", libro.getComentario());
                intent.putExtra("urlPortada", libro.getUrlPortada());
                intent.putExtra("imagenBase64", libro.getImagenBase64());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     /**
     * Muestra diálogo de confirmación para eliminar
     */
    private void mostrarDialogoEliminar(Libro libro) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_eliminar_title))
                .setMessage(getString(R.string.dialog_eliminar_message, libro.getTitulo()))
                .setPositiveButton(getString(R.string.dialog_eliminar_confirm), (dialog, which) -> {
                    repositorio.eliminarLibro(libro, new LibroRepository.OnOperacionListener() {
                        @Override
                        public void onExito(String mensaje) {
                            refrescarLista();
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String mensaje) {
                            Toast.makeText(requireContext(), "Error: " + mensaje,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton(getString(R.string.dialog_cancelar), null)
                .show();
    }

    /**
     * Refresca la lista de libros
     */
    public void refrescarLista() {
        if (adapter != null) {
            // Recargar desde el repositorio
            LibroRepository.obtenerInstancia().cargarLibros(
                    new LibroRepository.OnLibrosListener() {
                        @Override
                        public void onLibrosCargados(List<Libro> libros) {
                            adapter.actualizarLibros(LibroRepository.obtenerInstancia().obtenerLeidos());
                        }

                        @Override
                        public void onError(String mensaje) {
                            Toast.makeText(requireContext(), "Error: " + mensaje,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }
}