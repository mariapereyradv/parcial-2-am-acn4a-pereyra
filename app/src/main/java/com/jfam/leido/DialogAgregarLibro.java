package com.jfam.leido;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Diálogo para agregar un nuevo libro
 */
public class DialogAgregarLibro extends DialogFragment {

    private EditText etTitulo;
    private EditText etAutor;
    private EditText etEditorial;
    private EditText etIsbn;
    private EditText etComentario;
    private Button btnGuardar;
    private Button btnCancelar;
    private boolean esLeido;

    /**
     * Crea una nueva instancia del diálogo
     * @param esLeido true si se agrega a Leídos, false si es Deseado
     */
    public static DialogAgregarLibro nuevaInstancia(boolean esLeido) {
        DialogAgregarLibro dialogo = new DialogAgregarLibro();
        Bundle args = new Bundle();
        args.putBoolean("esLeido", esLeido);
        dialogo.setArguments(args);
        return dialogo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            esLeido = getArguments().getBoolean("esLeido", false);
        }
        // Estilo de diálogo de pantalla completa
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Leido_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.dialog_agregar_libro, container, false);

        inicializarVistas(vista);
        configurarBotones();

        return vista;
    }

    /**
     * Inicializa las referencias de las vistas
     */
    private void inicializarVistas(View vista) {
        etTitulo = vista.findViewById(R.id.etTitulo);
        etAutor = vista.findViewById(R.id.etAutor);
        etEditorial = vista.findViewById(R.id.etEditorial);
        etIsbn = vista.findViewById(R.id.etIsbn);
        etComentario = vista.findViewById(R.id.etComentario);
        btnGuardar = vista.findViewById(R.id.btnGuardar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
    }

    /**
     * Configura el comportamiento de los botones
     */
    private void configurarBotones() {
        btnGuardar.setOnClickListener(v -> guardarLibro());
        btnCancelar.setOnClickListener(v -> dismiss());
    }

    /**
     * Valida y guarda el nuevo libro
     */
    private void guardarLibro() {
        String titulo = etTitulo.getText().toString().trim();

        // Validación: título es obligatorio
        if (titulo.isEmpty()) {
            etTitulo.setError(getString(R.string.validation_titulo_required));
            etTitulo.requestFocus();
            return;
        }

        String autor = etAutor.getText().toString().trim();
        String editorial = etEditorial.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        // Crear y guardar el libro
        Libro nuevoLibro = new Libro(titulo, autor, editorial, isbn, comentario, esLeido);
        LibroRepository.obtenerInstancia(requireContext()).agregarLibro(nuevoLibro);

        // Refrescar la vista principal
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refrescarFragmentoActual();
        }

        String mensaje = esLeido ?
                getString(R.string.msg_libro_agregado_leidos) :
                getString(R.string.msg_libro_agregado_deseados);
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}