package com.example.semana06actividad21;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Uri sPhotoFileUri = null;

    // Lanzador para el Intent implícito de la cámara con máxima resolución (MediaStore.EXTRA_OUTPUT).
    private final ActivityResultLauncher<Intent> sLauncherFoto =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() != RESULT_OK) {
                            Toast.makeText(MainActivity.this, R.string.msg_foto_cancelada,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (sPhotoFileUri != null) {
                            ImageView IVFoto = findViewById(R.id.IVFoto);
                            IVFoto.setImageURI(sPhotoFileUri);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.msg_foto_sin_imagen,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Intent explícito: envía el nombre y la foto en alta resolución a AResultado.
        Button BTAceptar = findViewById(R.id.BTAceptar);
        BTAceptar.setOnClickListener(view -> {
            EditText ETNombreFoto = findViewById(R.id.ETNombreFoto);
            String StNombre = ETNombreFoto.getText().toString();
            if (StNombre.trim().isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.msg_falta_nombre, Toast.LENGTH_SHORT).show();
                return;
            }
            if (sPhotoFileUri == null) {
                Toast.makeText(MainActivity.this, R.string.msg_falta_foto, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent sIntent = new Intent(MainActivity.this, AResultado.class);
            sIntent.putExtra("STNombre", StNombre);
            sIntent.putExtra("STImageUri", sPhotoFileUri.toString());
            startActivity(sIntent);
        });

        // Intent implícito: abre la aplicación de cámara del sistema guardando la foto en alta resolución.
        Button BTHacerFoto = findViewById(R.id.BTHacerFoto);
        BTHacerFoto.setOnClickListener(view -> {
            Intent sIntentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File cacheDir = getCacheDir();
                File imageFile = new File(cacheDir, "full_captured_photo.jpg");
                sPhotoFileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
                sIntentCamara.putExtra(MediaStore.EXTRA_OUTPUT, sPhotoFileUri);
                sIntentCamara.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sLauncherFoto.launch(sIntentCamara);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, R.string.msg_sin_camara, Toast.LENGTH_SHORT).show();
            }
        });

        // Limpia el campo de texto y restaura la imagen inicial.
        Button BTLimpiar = findViewById(R.id.BTLimpiar);
        BTLimpiar.setOnClickListener(view -> {
            EditText ETNombreFoto = findViewById(R.id.ETNombreFoto);
            ETNombreFoto.setText("");
            sPhotoFileUri = null;
            ImageView IVFoto = findViewById(R.id.IVFoto);
            IVFoto.setImageResource(R.mipmap.ic_launcher);
        });
    }
}
