package com.jfam.leido;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetalleLibroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

        // Recibir datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String autor = getIntent().getStringExtra("autor");
        String editorial = getIntent().getStringExtra("editorial");
        String isbn = getIntent().getStringExtra("isbn");
        String comentario = getIntent().getStringExtra("comentario");
        String urlPortada = getIntent().getStringExtra("urlPortada");
        String base64 = getIntent().getStringExtra("imagenBase64");

        // Referencias
        ImageView imgPortada = findViewById(R.id.imgPortadaDetalle);
        TextView txtTitulo = findViewById(R.id.txtTituloDetalle);
        TextView txtAutor = findViewById(R.id.txtAutorDetalle);
        TextView txtEditorial = findViewById(R.id.txtEditorialDetalle);
        TextView txtIsbn = findViewById(R.id.txtIsbnDetalle);
        TextView txtComentario = findViewById(R.id.txtComentarioDetalle);
        Button btnCerrar = findViewById(R.id.btnCerrar);

        // Cargar datos
        txtTitulo.setText(titulo);
        txtAutor.setText(autor.isEmpty() ? "Autor desconocido" : autor);
        txtEditorial.setText(editorial.isEmpty() ? "No especificada" : editorial);
        txtIsbn.setText(isbn.isEmpty() ? "No disponible" : isbn);
        txtComentario.setText(comentario.isEmpty() ? "Sin notas" : comentario);

        // Cargar imagen
        if (base64 != null && !base64.isEmpty()) {
            byte[] decodedString = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            imgPortada.setImageBitmap(bitmap);
        } else if (urlPortada != null && !urlPortada.isEmpty()) {
            Glide.with(this)
                    .load(urlPortada)
                    .placeholder(R.color.primary_light)
                    .error(R.color.primary_light)
                    .centerCrop()
                    .into(imgPortada);
        }

        btnCerrar.setOnClickListener(v -> finish());
    }
}
