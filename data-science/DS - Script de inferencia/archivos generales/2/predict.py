import argparse
import json
import time
import numpy as np
import joblib

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--model", default="artifacts/sentiment_pipeline.joblib")
    parser.add_argument("--text", required=True)
    parser.add_argument("--pretty", action="store_true")
    args = parser.parse_args()

    t0 = time.perf_counter()
    pipeline = joblib.load(args.model)

    probs = pipeline.predict_proba([args.text])[0]
    idx = int(np.argmax(probs))
    pred = pipeline.classes_[idx]
    latency_ms = (time.perf_counter() - t0) * 1000.0

    out = {
        "prediction": str(pred),
        "probability": float(probs[idx]),
        "probs": {str(c): float(p) for c, p in zip(pipeline.classes_, probs)},
        "latency_ms": round(latency_ms, 3)
    }

    if args.pretty:
        print(json.dumps(out, ensure_ascii=False, indent=2))
    else:
        print(json.dumps(out, ensure_ascii=False))

if __name__ == "__main__":
    main()
