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
                Toast.makeText(requireContext(), "Detalle: " + libro.getTitulo(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Muestra diálogo de confirmación para eliminar
     */
    private void mostrarDialogoEliminar(Libro libro) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar")
                .setMessage("¿Eliminar \"" + libro.getTitulo() + "\" de Leídos?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    repositorio.eliminarLibro(libro);
                    refrescarLista();
                    Toast.makeText(requireContext(), "Libro eliminado",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
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