import argparse
import json
import sys
import time
from pathlib import Path

import joblib
import numpy as np


def validar_input(texto: str, min_len: int = 3) -> None:
    if texto is None:
        raise ValueError("El campo 'text' no puede ser null.")
    if not isinstance(texto, str):
        raise ValueError("El campo 'text' debe ser string.")
    if len(texto.strip()) < min_len:
        raise ValueError(f"El campo 'text' es demasiado corto (min {min_len} caracteres).")


def cargar_pipeline(pipeline_path: Path):
    if not pipeline_path.exists():
        raise FileNotFoundError(f"No se encontró el pipeline en: {pipeline_path.resolve()}")

    pipeline = joblib.load(pipeline_path)

    # Requisitos fuertes del MVP
    if not hasattr(pipeline, "predict_proba"):
        raise TypeError("El pipeline no tiene predict_proba(). Asegúrate de exportar un Pipeline completo.")
    if not hasattr(pipeline, "classes_"):
        raise TypeError("El pipeline no tiene classes_. Se requiere para mapear probabilidades a etiquetas.")

    return pipeline


def predecir(texto: str, pipeline) -> dict:
    validar_input(texto)

    t0 = time.perf_counter()

    probas = pipeline.predict_proba([texto])[0]  # shape: (n_clases,)
    classes = list(pipeline.classes_)            # e.g. ['negativo','neutro','positivo']

    idx = int(np.argmax(probas))
    pred_label = str(classes[idx])
    pred_proba = float(probas[idx])

    latency_ms = (time.perf_counter() - t0) * 1000.0

    probs_dict = {str(c): round(float(p), 4) for c, p in zip(classes, probas)}

    return {
        "prediction": pred_label,
        "probability": round(pred_proba, 4),
        "probs": probs_dict,
        "latency_ms": round(latency_ms, 2),
    }


def main():
    p = argparse.ArgumentParser(
        description="Sentiment inference (standalone, ternario) usando Pipeline exportado en 1 solo .joblib."
    )
    p.add_argument("--text", type=str, default=None, help="Texto a clasificar. Si no se envía, se leerá desde stdin.")
    p.add_argument("--pretty", action="store_true")
    p.add_argument("--pipeline", type=str, default="artifacts/sentiment_pipeline.joblib",
                   help="Ruta al Pipeline exportado (.joblib).")
    args = p.parse_args()

    texto = args.text if args.text is not None else sys.stdin.read()

    try:
        validar_input(texto)
        pipeline = cargar_pipeline(Path(args.pipeline))
        result = predecir(texto, pipeline)

        print(json.dumps(result, ensure_ascii=False, indent=2 if args.pretty else None))

        # warning opcional si te interesa monitorear latencia
        if result["latency_ms"] > 1000:
            print(
                json.dumps({"warning": "Latencia > 1s. Revisar hardware/config."}, ensure_ascii=False),
                file=sys.stderr,
            )

        return 0

    except Exception as e:
        print(json.dumps({"error": str(e)}, ensure_ascii=False), file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
