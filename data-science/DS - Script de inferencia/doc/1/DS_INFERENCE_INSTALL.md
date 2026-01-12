# DS Inference Script (Sentimiento) — Guía de instalación y uso (para Backend)

Este módulo contiene un **script de inferencia en Python** que toma un texto y devuelve un **JSON** con la predicción de sentimiento.
La intención de este documento es que el equipo de backend (Java / Spring Boot) pueda **instalarlo y ejecutarlo sin fricción**.

---

## 1) Estructura esperada

Dentro del repo (o del submódulo) debe existir:

```
ds_inference_script/
  artifacts/
    modelo_sentimiento.joblib
    tfidf_vectorizer.joblib
  scripts/
    predict.py
  requirements.txt
  text_utils.py
```

> **Importante:** Los archivos en `artifacts/` son obligatorios para inferencia.  
> Si faltan, el script no podrá cargar modelo/vectorizer.

---

## 2) Requisitos

### Recomendado (más estable para DS)
- **Python 3.11.x**

### También funciona (según compatibilidad de dependencias)
- Python 3.10.x

> ⚠️ Nota: En algunos entornos, **Python 3.13** puede dar problemas al instalar dependencias típicas de DS.  
> Si aparece un error con `numpy`, `scikit-learn`, etc., usa Python **3.11**.

---

## 3) Instalación (Windows / Linux / macOS)

### Opción A — PyCharm (reutilizar `.venv` existente) ✅ (la que estás usando)

Si PyCharm muestra:
> “Environment `.venv` already exists in the specified folder”

Significa que el entorno virtual ya está creado; solo hay que **seleccionarlo**.

1. `File → Settings…`
2. `Project: <tu proyecto> → Python Interpreter`
3. `Add Interpreter`
4. Seleccionar **Select existing**
5. Elegir este ejecutable:

**Windows**
```
<RUTA_PROYECTO>\.venv\Scripts\python.exe
```

Ejemplo:
```
C:\Users\Lap\PycharmProjects\PythonProject\.venv\Scripts\python.exe
```

6. `OK → Apply → OK`

✅ Verificación: en la esquina inferior derecha debe verse algo como `Python 3.x (.venv)`.

---

### Opción B — Terminal (crear venv desde cero)

> Útil si NO usas PyCharm o quieres un entorno limpio.

#### Windows (PowerShell)
```bash
cd ds_inference_script
py -3.11 -m venv .venv
.\.venv\Scripts\activate
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
```

#### Linux / macOS
```bash
cd ds_inference_script
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
```

---

## 4) Instalar dependencias (requirements.txt)

Desde terminal (PyCharm o sistema), ejecutar:

```bash
cd ds_inference_script
python -m pip install -r requirements.txt
```

### Verificación rápida
```bash
python -c "import joblib; print('joblib OK')"
```

---

## 5) Ejecutar inferencia (CLI)

> Ejecuta desde `ds_inference_script/`.

### Caso 1: Texto por parámetro
```bash
python scripts/predict.py --text "Me encantó el servicio, muy rápido" --pretty
```

### Caso 2: Texto por STDIN (recomendado para integración backend)
**Windows (PowerShell):**
```powershell
"Esto fue terrible, no lo recomiendo" | python scripts/predict.py --pretty
```

**Linux/macOS:**
```bash
echo "Esto fue terrible, no lo recomiendo" | python scripts/predict.py --pretty
```

---

## 6) Salida esperada (JSON)

La salida es un JSON por `stdout`. Un ejemplo típico:

```json
{
  "label": "positivo",
  "score": 0.87,
  "threshold": 0.50,
  "latency_ms": 42
}
```

> El contenido exacto depende de cómo esté implementado `predict.py`.  
> El backend debe tratarlo como JSON parseable (UTF-8).

---

## 7) Artefactos del modelo

Por defecto, el script suele usar rutas relativas como:

- `artifacts/tfidf_vectorizer.joblib`
- `artifacts/modelo_sentimiento.joblib`

Si en algún entorno cambian las rutas, preferir pasar parámetros (si el script los soporta):

```bash
python scripts/predict.py \
  --text "Excelente atención" \
  --vectorizer "artifacts/tfidf_vectorizer.joblib" \
  --model "artifacts/modelo_sentimiento.joblib" \
  --pretty
```

---

## 8) Integración sugerida con Spring Boot (Java)

### Enfoque recomendado: llamar al script por STDIN y leer JSON por STDOUT

- **Ventajas**: simple, sin servidor Python, fácil de desplegar.
- **Contras**: cada request crea proceso (puede ser más lento si hay alto tráfico).

#### Ejemplo con `ProcessBuilder` (Java)

```java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class PythonPredictClient {

  public static String predict(String pythonExe, String workingDir, String text) throws Exception {
    ProcessBuilder pb = new ProcessBuilder(
        pythonExe,
        "scripts/predict.py",
        "--pretty"
    );

    pb.directory(new File(workingDir));
    pb.redirectErrorStream(false); // stderr separado para debug
    Process p = pb.start();

    // Enviar texto por STDIN (UTF-8)
    try (OutputStream os = p.getOutputStream()) {
      os.write(text.getBytes(StandardCharsets.UTF_8));
    }

    // Leer STDOUT (JSON)
    String stdout;
    try (InputStream is = p.getInputStream()) {
      stdout = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    // Leer STDERR (si existiera)
    String stderr;
    try (InputStream es = p.getErrorStream()) {
      stderr = new String(es.readAllBytes(), StandardCharsets.UTF_8);
    }

    boolean finished = p.waitFor(10, TimeUnit.SECONDS);
    if (!finished) {
      p.destroyForcibly();
      throw new RuntimeException("Timeout ejecutando predict.py");
    }

    if (p.exitValue() != 0) {
      throw new RuntimeException("predict.py falló. stderr=" + stderr);
    }

    return stdout.trim();
  }
}
```

**Valores típicos:**
- `pythonExe`:
  - Windows: `C:\...\ds_inference_script\.venv\Scripts\python.exe`
  - Linux/macOS: `/.../ds_inference_script/.venv/bin/python`
- `workingDir`: ruta absoluta a `ds_inference_script`

Luego el backend parsea el JSON con Jackson/Gson y responde a su API.

> Sugerencia práctica: versionar en `application.yml` la ruta de `pythonExe` y `workingDir` por ambiente (dev/staging/prod).

---

## 9) Docker (opcional)

Si el equipo prefiere evitar instalaciones locales, se puede contenerizar.

### Dockerfile (ejemplo mínimo)
Crea `ds_inference_script/Dockerfile`:

```dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY . /app

RUN pip install --no-cache-dir -r requirements.txt

# Ejemplo: ejecutar con STDIN
# echo "texto" | docker run -i <image>
CMD ["python", "scripts/predict.py", "--pretty"]
```

### Build & Run
```bash
cd ds_inference_script
docker build -t ds-inference:latest .
echo "Me encantó" | docker run -i ds-inference:latest
```

---

## 10) Troubleshooting

### A) `pip` no se reconoce / instala en otro Python
Usar siempre:
```bash
python -m pip install -r requirements.txt
```

### B) `No module named ...`
Causa: el entorno activo no es el correcto.  
Solución:
- En PyCharm: confirmar intérprete `.venv`
- Reinstalar:
```bash
cd ds_inference_script
python -m pip install -r requirements.txt
```

### C) Problemas con Python 3.13
Solución más estable:
- Instalar **Python 3.11**
- Recrear venv con 3.11
- Reinstalar requirements

---

## 11) Checklist final (antes de entregar)

- [ ] Existe `ds_inference_script/requirements.txt`
- [ ] Existen los `.joblib` en `ds_inference_script/artifacts/`
- [ ] `python -m pip install -r requirements.txt` sin errores
- [ ] `python scripts/predict.py --text "test" --pretty` devuelve JSON
- [ ] Backend puede ejecutar el script (STDIN/STDOUT) y parsear JSON

---
