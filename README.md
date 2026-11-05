# Actividad 2.1 — Intents (IEI-093)

Aplicación Android en **Java** que demuestra la comunicación entre `Activity` mediante
**Intents explícitos e implícitos**, tomando una foto con la cámara del sistema y
compartiendo el resultado con otras aplicaciones.

- Asignatura: IEI-093 — Programación Android (IP-CFT Santo Tomás, sede Ovalle)
- Actividad: Unidad 2, Semana 6, Actividad 2.1
- Autor: Samuel Bugueño Vega (Nek)

## 1. Qué hace la aplicación

**MainActivity** — pantalla principal:

1. **Foto con la cámara del sistema (intent implícito)**
   - Botón *HACER FOTO*: lanza `MediaStore.ACTION_IMAGE_CAPTURE` con `MediaStore.EXTRA_OUTPUT`
     para guardar la foto en **alta resolución** en un archivo propio (`Uri` private de la app),
     no en el miniatura que devuelve la cámara.
   - Otorga permisos de lectura/escritura del archivo a la app de cámara con
     `FLAG_GRANT_READ_URI_PERMISSION` / `FLAG_GRANT_WRITE_URI_PERMISSION`.
2. **Enviar el resultado (intent explícito)** — Botón *MOSTRAR FOTO*:
   - `Intent` explícito hacia `AResultado` con el **nombre de la foto**
     (`putExtra("STNombre", ...)`) y la da la dirección del archivo (`putExtra("STImageUri", ...)`).
   - Botón *LIMPIAR* para resetear el formulario y la vista previa.

**AResultado** — pantalla de resultado:

1. Muestra el nombre recibido con `getIntent().getStringExtra(...)`.
2. Carga la foto con `Uri.parse(uriString)` en un `ImageView`.
3. **Compartir con otras apps (intent implícito)** — usa `Intent.ACTION_SEND` para compartir
   el nombre y la foto con WhatsApp, correo, etc.

## 2. Requisitos de la pauta y dónde se cumplen

| Requisito de la pauta | Dónde está en el código |
| --- | --- |
| Intent explícito entre Activities | `MainActivity` → `AResultado` (con extras) |
| Intent implícito a otra app | Cámara (`MediaStore.ACTION_IMAGE_CAPTURE`) y compartir (`ACTION_SEND`) |
| Paso de datos entre Activities | `putExtra` / `getStringExtra` (nombre y `Uri` de la foto) |

## 3. Cómo compilar

```bash
# Con Android Studio: File > Open > seleccionar esta carpeta.
# Por consola (necesita JDK 17 y ANDROID_HOME apuntando al SDK):
./gradlew assembleDebug
```

- `minSdk`, `targetSdk` y `compileSdk` definidos en `app/build.gradle.kts`.

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/semana06actividad21/
│   ├── MainActivity.java        # intents explícitos e implícitos
│   └── AResultado.java          # destino del intent explícito + compartir
└── res/layout/
    ├── activity_main.xml
    └── activity_resultado.xml
```

## 4. Continuidad

Esta actividad es la base de la Actividad 2.2:
[Semana7Actividad22ThreadsYSensores](https://github.com/samueleduardobuguenovega-lang/Semana7Actividad22ThreadsYSensores),
que integra sobre estos intents **threads**, **AsyncTask** y **sensores**.

---

*Actividad de curso abierta al público — mis repos de curso están abiertos, con sus errores y todo.*
