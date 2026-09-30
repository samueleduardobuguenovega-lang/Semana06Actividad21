package com.example.semana06actividad21;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Lanzador para el Intent implicito de la camara (MediaStore.ACTION_IMAGE_CAPTURE).
    private final ActivityResultLauncher<Intent> sLauncherFoto =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                                Toast.makeText(MainActivity.this, R.string.msg_foto_cancelada,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            Bundle sExtras = result.getData().getExtras();
                            Object sDato = (sExtras != null) ? sExtras.get("data") : null;
                            if (!(sDato instanceof Bitmap)) {
                                Toast.makeText(MainActivity.this, R.string.msg_foto_sin_imagen,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            ImageView IVFoto = (ImageView) findViewById(R.id.IVFoto);
                            IVFoto.setImageBitmap((Bitmap) sDato);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                new androidx.core.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                        return insets;
                    }
                });

        // Intent explicito: envia el nombre de la foto a AResultado.
        final Button BTAceptar = (Button) findViewById(R.id.BTAceptar);
        BTAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ETNombreFoto = (EditText) findViewById(R.id.ETNombreFoto);
                String StNombre = ETNombreFoto.getText().toString();
                if (StNombre.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.msg_falta_nombre, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent sIntent = new Intent(MainActivity.this, AResultado.class);
                sIntent.putExtra("STNombre", StNombre);
                startActivity(sIntent);
            }
        });

        // Intent implicito: abre la aplicacion de camara del sistema.
        Button BTHacerFoto = (Button) findViewById(R.id.BTHacerFoto);
        BTHacerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sIntentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    sLauncherFoto.launch(sIntentCamara);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, R.string.msg_sin_camara, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Limpia el campo de texto y restaura la imagen inicial.
        Button BTLimpiar = (Button) findViewById(R.id.BTLimpiar);
        BTLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ETNombreFoto = (EditText) findViewById(R.id.ETNombreFoto);
                ETNombreFoto.setText("");
                ImageView IVFoto = (ImageView) findViewById(R.id.IVFoto);
                IVFoto.setImageResource(R.mipmap.ic_launcher);
            }
        });
    }
}
