#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script de NOTICIAS EDUCATIVAS — WEB SCRAPING (no RSS).

Fuente unica:  https://www.educaciontrespuntocero.com/noticias/

Tecnica:
  1) Peticion HTTP a la pagina HTML del listado de noticias (la pagina
     "para humanos", NO al feed RSS).
  2) Parseo del DOM con BeautifulSoup4 + lxml.
  3) Extraccion por selectores CSS de las tarjetas del listado
     (titulo, URL, fecha, resumen).
  4) Una segunda peticion HTTP por cada articulo para extraer el cuerpo
     completo navegando su <div class="entry-content">.
  5) Concurrencia con ThreadPoolExecutor: varios articulos a la vez.

Esto SI es web scraping (HTML para humanos + DOM parsing + selectores).
El parsing de RSS (que era la version anterior) NO es scraping porque
el sitio ya te entrega los datos pre-estructurados.

Salida por linea (7 campos separados por |||):
    titulo|||url|||fecha|||fuente|||descripcion|||seccion|||cuerpo

Log detallado en  C:\\Muudle_Tools\\Logs\\noticias_debug.log
"""

import sys
import os
import re
import io
import ssl
import urllib.request
import urllib.error
import traceback
import datetime
import random
import platform
from concurrent.futures import ThreadPoolExecutor, as_completed

# Libreria de scraping. Instalar con:  pip install beautifulsoup4 lxml
try:
    from bs4 import BeautifulSoup
except ImportError:
    print("Sin libreria|||#|||||||Falta dependencia 'beautifulsoup4'. "
          "Instalala con:  pip install beautifulsoup4 lxml|||Informacion|||")
    sys.exit(0)

# ---------------------------------------------------------------------------
# Logger
# ---------------------------------------------------------------------------
LOG_DIR  = r"C:\Muudle_Tools\Logs"
LOG_PATH = os.path.join(LOG_DIR, "noticias_debug.log")
_log_fp = None


def _init_log():
    global _log_fp
    try:
        os.makedirs(LOG_DIR, exist_ok=True)
        _log_fp = open(LOG_PATH, "w", encoding="utf-8", errors="replace")
    except Exception:
        _log_fp = None


def log(msg, *args):
    if args:
        try: msg = msg % args
        except Exception: msg = str(msg) + " " + " ".join(str(a) for a in args)
    ts = datetime.datetime.now().strftime("%H:%M:%S.%f")[:-3]
    if _log_fp is not None:
        try: _log_fp.write(f"[{ts}] {msg}\n"); _log_fp.flush()
        except Exception: pass


def log_exc(ctx):
    log("EXCEPTION en %s:\n%s", ctx, traceback.format_exc())


# ---------------------------------------------------------------------------
# Configuracion del scraper
# ---------------------------------------------------------------------------
URL_LISTADO   = "https://www.educaciontrespuntocero.com/noticias/"
FUENTE_NOMBRE = "Educacion 3.0"
SECCION       = "Tecnologia Educativa"

MAX_NOTICIAS    = 15        # cuantas tarjetas del listado procesar
SCRAPE_WORKERS  = 6         # peticiones concurrentes a paginas de articulo
TIMEOUT_LISTADO = 15
TIMEOUT_ART     = 10
MAX_DESC        = 320       # tope del resumen

HEADERS = {
    "User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                   "AppleWebKit/537.36 (KHTML, like Gecko) "
                   "Chrome/124.0.0.0 Safari/537.36"),
    "Accept": ("text/html,application/xhtml+xml,application/xml;q=0.9,"
               "*/*;q=0.8"),
    "Accept-Language": "es-ES,es;q=0.9,en;q=0.5",
    "Accept-Encoding": "identity",
    "Cache-Control": "no-cache",
}


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
def ssl_ctx():
    ctx = ssl.create_default_context()
    ctx.check_hostname = False
    ctx.verify_mode = ssl.CERT_NONE
    return ctx


def fetch_html(url, timeout, referer=None):
    """GET de una URL y devuelve el HTML decodificado como string."""
    h = dict(HEADERS)
    if referer:
        h["Referer"] = referer
    req = urllib.request.Request(url, headers=h)
    with urllib.request.urlopen(req, context=ssl_ctx(), timeout=timeout) as r:
        raw = r.read()
    try:
        return raw.decode("utf-8")
    except UnicodeDecodeError:
        return raw.decode("utf-8", errors="replace")


def sanear(t):
    return (t or "").replace("|||", " ").replace("\n", " ").replace("\r", "").strip()


def sanear_cuerpo(t):
    """Mantiene los \\n del cuerpo (para reconstruir parrafos) pero quita |||."""
    return (t or "").replace("|||", " ").replace("\r", "").strip()


def fecha_humana(iso):
    """Convierte '2026-05-18T14:00:00+02:00' en '18 May 2026 14:00'."""
    if not iso:
        return ""
    try:
        # tolerante a varias formas
        dt = datetime.datetime.fromisoformat(iso.replace("Z", "+00:00"))
        return dt.strftime("%d %b %Y %H:%M")
    except Exception:
        return iso[:16]


# ---------------------------------------------------------------------------
# PASO 1 — Scrapear el LISTADO de noticias (pagina HTML)
# ---------------------------------------------------------------------------
def scrape_listado():
    """
    Descarga la pagina del listado y extrae las primeras MAX_NOTICIAS tarjetas.
    Devuelve lista de dicts con titulo, url, fecha (ISO), resumen.
    """
    log("Fetch LISTADO -> %s", URL_LISTADO)
    html = fetch_html(URL_LISTADO, TIMEOUT_LISTADO)
    log("  HTML recibido: %d KB", len(html) // 1024)

    soup = BeautifulSoup(html, "lxml")
    titulos = soup.select("h2.entry-title, h3.entry-title")
    log("  selectores h2/h3.entry-title -> %d tarjetas", len(titulos))

    noticias = []
    for h in titulos[:MAX_NOTICIAS]:
        # Titulo + URL: estan dentro del <a> del propio h
        a = h.find("a", href=True)
        if not a:
            continue
        titulo = a.get_text(strip=True)
        url = a["href"]
        if not titulo or not url:
            continue

        # Fecha: subir al ancestro y localizar el primer <time> que tenga datetime
        fecha_iso = ""
        cont = h
        for _ in range(6):
            cont = cont.parent
            if cont is None:
                break
            t = cont.find("time")
            if t and t.get("datetime"):
                fecha_iso = t["datetime"]
                break

        # Resumen: el siguiente sibling con class entry-summary
        resumen = ""
        sib = h.find_next_sibling()
        # A veces hay un wrapper intermedio (figure, div); buscamos primer p.entry-summary cercano
        if sib is None or "entry-summary" not in (sib.get("class") or []):
            for cand in h.parent.find_all("p", class_="entry-summary", limit=1):
                sib = cand; break
        if sib is not None:
            resumen = sib.get_text(strip=True)

        noticias.append({
            "titulo":  titulo,
            "url":     url,
            "fecha":   fecha_iso,
            "resumen": resumen[:MAX_DESC],
        })

    log("  -> %d noticias validas en el listado", len(noticias))
    return noticias


# ---------------------------------------------------------------------------
# PASO 2 — Scrapear el CUERPO de cada articulo (peticion individual)
# ---------------------------------------------------------------------------
def scrape_cuerpo(url):
    """
    Descarga la pagina del articulo y extrae el cuerpo en texto plano.
    Devuelve los parrafos unidos por doble salto de linea.
    """
    try:
        html = fetch_html(url, TIMEOUT_ART, referer=URL_LISTADO)
    except urllib.error.HTTPError as e:
        log("  %s -> HTTP %s", url[:60], e.code)
        return ""
    except Exception as e:
        log("  %s -> %s", url[:60], e)
        return ""

    soup = BeautifulSoup(html, "lxml")
    contenedor = soup.select_one("div.entry-content")
    if contenedor is None:
        log("  %s -> sin <div.entry-content>", url[:60])
        return ""

    # Eliminar bloques que ensucian la lectura: figuras, asides, scripts, ads
    for tag in contenedor.select("figure, aside, script, style, .wp-block-image, "
                                 ".sharedaddy, .related-posts, .ads, [class*='ad-']"):
        tag.decompose()

    bloques = []
    # Iteramos en orden por parrafos, subtitulos y elementos de lista
    for el in contenedor.find_all(["p", "h2", "h3", "h4", "li", "blockquote"]):
        texto = el.get_text(" ", strip=True)
        if not texto or len(texto) < 20:
            continue
        tag = el.name
        if tag in ("h2", "h3", "h4"):
            bloques.append("\n# " + texto)
        elif tag == "li":
            bloques.append("- " + texto)
        else:
            bloques.append(texto)
    return "\n\n".join(bloques).strip()


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
def main():
    _init_log()
    log("================ INICIO (WEB SCRAPING) ================")
    log("Python:   %s", sys.version.replace("\n", " "))
    log("Platform: %s", platform.platform())
    log("Tecnica:  HTTP GET + BeautifulSoup + selectores CSS + concurrencia")
    log("Listado:  %s", URL_LISTADO)

    if hasattr(sys.stdout, "buffer"):
        sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8",
                                       errors="replace")

    # PASO 1: listado
    try:
        noticias = scrape_listado()
    except Exception:
        log_exc("scrape_listado")
        noticias = []

    if not noticias:
        log("RESULTADO: 0 noticias.")
        print("Sin conexion|||#|||||||No se pudo scrapear el listado de "
              "educaciontrespuntocero.com. Detalles en "
              "C:\\Muudle_Tools\\Logs\\noticias_debug.log|||Informacion|||")
        log("================ FIN ================")
        return

    # Mezclar para variedad
    random.shuffle(noticias)

    # PASO 2: cuerpos en paralelo
    log("Scraping cuerpos en paralelo (workers=%d)...", SCRAPE_WORKERS)
    cuerpos = {}
    try:
        with ThreadPoolExecutor(max_workers=SCRAPE_WORKERS) as ex:
            futs = {ex.submit(scrape_cuerpo, n["url"]): i
                    for i, n in enumerate(noticias)}
            ok = 0
            for fut in as_completed(futs, timeout=60):
                i = futs[fut]
                try:
                    cuerpos[i] = fut.result()
                    if cuerpos[i]:
                        ok += 1
                except Exception:
                    cuerpos[i] = ""
        log("  %d/%d cuerpos extraidos correctamente", ok, len(noticias))
    except Exception:
        log_exc("ThreadPoolExecutor cuerpos")

    # PASO 3: emitir lineas
    emitidas = 0
    for i, n in enumerate(noticias):
        cuerpo = cuerpos.get(i, "")
        cuerpo_codif = cuerpo.replace("\n", "\\n")
        print(f"{sanear(n['titulo'])}|||{sanear(n['url'])}|||"
              f"{sanear(fecha_humana(n['fecha']))}|||{sanear(FUENTE_NOMBRE)}|||"
              f"{sanear(n['resumen'])}|||{sanear(SECCION)}|||"
              f"{sanear_cuerpo(cuerpo_codif)}",
              flush=True)
        emitidas += 1

    log("Emitidas a Java: %d noticias", emitidas)
    log("================ FIN ================")


if __name__ == "__main__":
    try:
        main()
    except Exception:
        try: _init_log(); log_exc("main() top-level")
        except Exception: pass
        print("Error inesperado|||#|||||||Mira "
              "C:\\Muudle_Tools\\Logs\\noticias_debug.log|||Informacion|||")
