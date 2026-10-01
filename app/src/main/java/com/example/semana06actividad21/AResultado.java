package com.example.semana06actividad21;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AResultado extends AppCompatActivity {

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        TextView TVNombreRecibido = findViewById(R.id.TVNombreRecibido);
        ImageView IVFotoResultado = findViewById(R.id.IVFotoResultado);

        // Recibe el dato enviado desde MainActivity mediante el Intent explícito.
        String StNombre = getIntent().getStringExtra("STNombre");
        if (StNombre == null || StNombre.trim().isEmpty()) {
            TVNombreRecibido.setText(R.string.resultado_sin_nombre);
        } else {
            TVNombreRecibido.setText(getString(R.string.resultado_prefijo, StNombre));
        }

        String uriString = getIntent().getStringExtra("STImageUri");
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            IVFotoResultado.setImageURI(imageUri);
        }

        // Intent implícito: comparte el nombre y la foto con otras aplicaciones (WhatsApp, etc.).
        Button BTCompartir = findViewById(R.id.BTCompartir);
        BTCompartir.setOnClickListener(view -> {
            String StValor = TVNombreRecibido.getText().toString();
            Intent sIntentEnviar = new Intent(Intent.ACTION_SEND);

            if (imageUri != null) {
                sIntentEnviar.setType("image/*");
                sIntentEnviar.putExtra(Intent.EXTRA_STREAM, imageUri);
                sIntentEnviar.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                sIntentEnviar.setType("text/plain");
            }

            sIntentEnviar.putExtra(Intent.EXTRA_TEXT, StValor);

            Intent sChooser = Intent.createChooser(sIntentEnviar, getString(R.string.titulo_compartir));
            try {
                startActivity(sChooser);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(AResultado.this, R.string.msg_sin_app_compartir, Toast.LENGTH_SHORT).show();
            }
        });

        // Cierra la Activity y vuelve a la anterior.
        Button BTVolver = findViewById(R.id.BTVolver);
        BTVolver.setOnClickListener(view -> finish());
    }
}
