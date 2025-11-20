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
 * Fragmento que muestra la lista de libros deseados
 * VERSIÓN SIN EMPTY STATE (para evitar errores)
 */
public class FragmentDeseados extends Fragment {

    private RecyclerView recyclerView;
    private LibroAdapter adapter;
    private LibroRepository repositorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_deseados, container, false);

        repositorio = LibroRepository.obtenerInstancia(requireContext());
        recyclerView = vista.findViewById(R.id.recyclerDeseados);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        configurarAdapter();
        refrescarLista();

        return vista;
    }

    /**
     * Configura el adaptador con los listeners
     */
    private void configurarAdapter() {
        adapter = new LibroAdapter(repositorio.obtenerDeseados(), new LibroAdapter.OnLibroListener() {
            @Override
            public void alMantenerPresionado(Libro libro) {
                mostrarDialogoOpciones(libro);
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
     * Muestra diálogo con opciones: Marcar como leído / Eliminar
     */
    private void mostrarDialogoOpciones(Libro libro) {
        String[] opciones = {
                getString(R.string.dialog_opciones_marcar),
                getString(R.string.dialog_opciones_eliminar),
                getString(R.string.dialog_cancelar)
        };

        new AlertDialog.Builder(requireContext())
                .setTitle(libro.getTitulo())
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Marcar como leído
                            repositorio.cambiarEstado(libro);
                            refrescarLista();
                            refrescarFragmentoLeidos();
                            Toast.makeText(requireContext(),
                                    getString(R.string.msg_marcado_leido, libro.getTitulo()),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Eliminar
                            repositorio.eliminarLibro(libro);
                            refrescarLista();
                            Toast.makeText(requireContext(),
                                    getString(R.string.libro_eliminado_deseados),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 2: // Cancelar
                            break;
                    }
                })
                .show();
    }

    /**
     * Refresca el fragmento de Leídos si está visible
     */
    private void refrescarFragmentoLeidos() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refrescarFragmentoActual();
        }
    }

    /**
     * Refresca la lista de libros
     */
    public void refrescarLista() {
        if (adapter != null) {
            adapter.actualizarLibros(repositorio.obtenerDeseados());
        }
    }
}