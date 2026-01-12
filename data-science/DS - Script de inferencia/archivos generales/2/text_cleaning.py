import re

_URL_RE = re.compile(r"(https?://\S+|www\.\S+)", flags=re.IGNORECASE)
_MENTION_RE = re.compile(r"@\w+", flags=re.UNICODE)
_NON_ALNUM_ES_RE = re.compile(r"[^0-9a-záéíóúüñ\s]", flags=re.IGNORECASE)

def limpiar_texto(texto: str) -> str:
    """Limpieza mínima para NLP (MVP):
    - minúsculas
    - remover URLs y menciones
    - remover caracteres especiales (conservando acentos)
    - normalizar espacios
    """
    if texto is None:
        return ""
    texto = str(texto).lower()
    texto = _URL_RE.sub(" ", texto)
    texto = _MENTION_RE.sub(" ", texto)
    texto = _NON_ALNUM_ES_RE.sub(" ", texto)
    texto = re.sub(r"\s+", " ", texto).strip()
    return texto
