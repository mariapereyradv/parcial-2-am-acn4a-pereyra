package com.jfam.leido;

import android.app.AlertDialog;
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

/**
 * Fragmento que muestra la lista de libros leídos
 * VERSIÓN SIN EMPTY STATE
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

        repositorio = LibroRepository.obtenerInstancia(requireContext());
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
                // TODO: Abrir detalle del libro (segunda entrega)
                Toast.makeText(requireContext(),
                        getString(R.string.detalle_prefix) + libro.getTitulo(),
                        Toast.LENGTH_SHORT).show();
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
                    repositorio.eliminarLibro(libro);
                    refrescarLista();
                    Toast.makeText(requireContext(), getString(R.string.msg_libro_eliminado),
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.dialog_cancelar), null)
                .show();
    }

    /**
     * Refresca la lista de libros
     */
    public void refrescarLista() {
        if (adapter != null) {
            adapter.actualizarLibros(repositorio.obtenerLeidos());
        }
    }
}