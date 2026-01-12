import re

def limpiar_texto(texto: str) -> str:
    s = str(texto).lower()
    s = re.sub(r"http\S+|www\.\S+", " ", s)
    s = re.sub(r"@\w+", " ", s)
    s = re.sub(r"[^\w\sáéíóúñü]", " ", s)
    s = re.sub(r"\s+", " ", s).strip()
    return s
