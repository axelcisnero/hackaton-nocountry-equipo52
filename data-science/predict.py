from flask import Flask, request, jsonify
import joblib
import numpy as np

# Carga UNA sola vez al iniciar el servicio
pipeline = joblib.load("sentiment_pipeline.joblib")

app = Flask(__name__)

@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json(silent=True)

    if not data or "text" not in data:
        return jsonify({"error": "text is required"}), 400

    text = data["text"]
    if text is None or str(text).strip() == "":
        return jsonify({"error": "text is required"}), 400

    # Predicción
    pred = pipeline.predict([text])[0]

    # Probabilidad (si el pipeline/modelo la soporta)
    probability = None
    if hasattr(pipeline, "predict_proba"):
        probs = pipeline.predict_proba([text])[0]
        probability = float(np.max(probs))

    return jsonify({
        "prediction": str(pred),
        "probability": probability
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)