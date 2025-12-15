package com.jfam.leido;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Diálogo para agregar un nuevo libro
 */
public class DialogAgregarLibro extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etTitulo;
    private EditText etAutor;
    private EditText etEditorial;
    private EditText etIsbn;
    private EditText etUrlPortada;
    private EditText etComentario;
    private ImageView imgVistaPrevia;
    private Button btnSeleccionarImagen;
    private Button btnUsarUrl;
    private Button btnGuardar;
    private Button btnCancelar;

    private boolean esLeido;
    private String imagenBase64 = "";
    private String urlPortada = "";

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

    private void inicializarVistas(View vista) {
        etTitulo = vista.findViewById(R.id.etTitulo);
        etAutor = vista.findViewById(R.id.etAutor);
        etEditorial = vista.findViewById(R.id.etEditorial);
        etIsbn = vista.findViewById(R.id.etIsbn);
        etUrlPortada = vista.findViewById(R.id.etUrlPortada);
        etComentario = vista.findViewById(R.id.etComentario);
        imgVistaPrevia = vista.findViewById(R.id.imgVistaPrevia);
        btnSeleccionarImagen = vista.findViewById(R.id.btnSeleccionarImagen);
        btnUsarUrl = vista.findViewById(R.id.btnUsarUrl);
        btnGuardar = vista.findViewById(R.id.btnGuardar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
    }

    private void configurarBotones() {
        btnGuardar.setOnClickListener(v -> guardarLibro());
        btnCancelar.setOnClickListener(v -> dismiss());

        // Botón para seleccionar imagen de galería
        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());

        // Botón para usar URL
        btnUsarUrl.setOnClickListener(v -> {
            if (etUrlPortada.getVisibility() == View.GONE) {
                etUrlPortada.setVisibility(View.VISIBLE);
                etUrlPortada.requestFocus();
            } else {
                cargarImagenDesdeUrl();
            }
        });

        // Listener para cargar imagen cuando se escribe URL
        etUrlPortada.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                cargarImagenDesdeUrl();
            }
        });
    }

    /**
     * Abre la galería del dispositivo
     */
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Carga imagen desde URL usando Glide
     */
    private void cargarImagenDesdeUrl() {
        String url = etUrlPortada.getText().toString().trim();
        if (!url.isEmpty()) {
            urlPortada = url;
            imagenBase64 = ""; // Limpiar base64 si había

            Glide.with(this)
                    .load(url)
                    .placeholder(R.color.primary_light)
                    .error(R.color.error)
                    .centerCrop()
                    .into(imgVistaPrevia);

            Toast.makeText(requireContext(), "Portada cargada desde URL",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            Uri imageUri = data.getData();

            try {
                // Convertir imagen a Base64
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Redimensionar para no ocupar tanto espacio
                Bitmap resized = redimensionarBitmap(bitmap, 400, 600);

                // Convertir a Base64
                imagenBase64 = bitmapToBase64(resized);
                urlPortada = ""; // Limpiar URL si había

                // Mostrar vista previa
                imgVistaPrevia.setImageBitmap(resized);

                Toast.makeText(requireContext(), "Imagen cargada correctamente",
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error al cargar imagen",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Redimensiona bitmap manteniendo proporción
     */
    private Bitmap redimensionarBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float ratio = Math.min(
                (float) maxWidth / width,
                (float) maxHeight / height
        );

        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    /**
     * Convierte Bitmap a String Base64
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Valida y guarda el nuevo libro
     */
    private void guardarLibro() {
        String titulo = etTitulo.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("El título es obligatorio");
            etTitulo.requestFocus();
            return;
        }

        String autor = etAutor.getText().toString().trim();
        String editorial = etEditorial.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        // Crear libro
        Libro nuevoLibro = new Libro(titulo, autor, editorial, isbn, comentario, esLeido);

        // Asignar portada
        if (!imagenBase64.isEmpty()) {
            nuevoLibro.setImagenBase64(imagenBase64);
        } else if (!urlPortada.isEmpty()) {
            nuevoLibro.setUrlPortada(urlPortada);
        }

        // Guardar en Firestore
        LibroRepository.obtenerInstancia().agregarLibro(nuevoLibro,
                new LibroRepository.OnOperacionListener() {
                    @Override
                    public void onExito(String mensaje) {
                        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

                        // Refrescar MainActivity - FORZAR RECARGA DESDE FIRESTORE
                        if (getActivity() instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            // Recargar datos desde Firestore
                            LibroRepository.obtenerInstancia().cargarLibros(
                                    new LibroRepository.OnLibrosListener() {
                                        @Override
                                        public void onLibrosCargados(List<Libro> libros) {
                                            mainActivity.refrescarFragmentoActual();
                                        }

                                        @Override
                                        public void onError(String error) {
                                            // Ignorar error silencioso
                                        }
                                    }
                            );
                        }

                        dismiss();
                    }

                    @Override
                    public void onError(String mensaje) {
                        Toast.makeText(requireContext(), "Error: " + mensaje,
                                Toast.LENGTH_SHORT).show();
                    }
                });
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