#!/usr/bin/env python3
"""Convertit les illustrations SVG en vector drawables Android.

Usage : python3 design/illustrations/vers_drawable.py
Chaque design/illustrations/<dossier>/<nom>.svg devient
app/src/main/res/drawable/illustration_<nom>.xml (tirets -> soulignés).

Sous-ensemble SVG pris en charge, celui des dessins du projet :
g, path, circle, ellipse, rect (rx), line, clipPath ;
fill, stroke, stroke-width, stroke-linecap, stroke-linejoin, opacity ;
transform rotate(a [cx cy]) et translate(x [y]).
"""

import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

RACINE = Path(__file__).resolve().parents[2]
SOURCES = RACINE / "design" / "illustrations"
SORTIE = RACINE / "app" / "src" / "main" / "res" / "drawable"
SVG = "{http://www.w3.org/2000/svg}"
HERITES = ("fill", "stroke", "stroke-width", "stroke-linecap", "stroke-linejoin")


def nombre(x):
    """Écrit un nombre sans zéros inutiles"""
    texte = f"{float(x):.3f}".rstrip("0").rstrip(".")
    return "0" if texte in ("-0", "") else texte


def chemin_cercle(cx, cy, rx, ry):
    """Ellipse en deux arcs"""
    return (f"M{nombre(cx - rx)},{nombre(cy)} a{nombre(rx)},{nombre(ry)} 0 1,0 {nombre(2 * rx)},0 "
            f"a{nombre(rx)},{nombre(ry)} 0 1,0 {nombre(-2 * rx)},0 Z")


def chemin_rect(x, y, l, h, r):
    """Rectangle aux coins arrondis, rayon borné comme en SVG"""
    r = min(r, l / 2, h / 2)
    if r <= 0:
        return f"M{nombre(x)},{nombre(y)} H{nombre(x + l)} V{nombre(y + h)} H{nombre(x)} Z"
    n = nombre
    return (f"M{n(x + r)},{n(y)} H{n(x + l - r)} A{n(r)},{n(r)} 0 0 1 {n(x + l)},{n(y + r)} "
            f"V{n(y + h - r)} A{n(r)},{n(r)} 0 0 1 {n(x + l - r)},{n(y + h)} H{n(x + r)} "
            f"A{n(r)},{n(r)} 0 0 1 {n(x)},{n(y + h - r)} V{n(y + r)} A{n(r)},{n(r)} 0 0 1 {n(x + r)},{n(y)} Z")


def chemin(el):
    """Géométrie d'un élément en pathData, ou None si l'élément n'en a pas"""
    a = lambda nom, defaut=0.0: float(el.get(nom, defaut))
    balise = el.tag.removeprefix(SVG)
    if balise == "path":
        return " ".join(el.get("d").split())
    if balise == "circle":
        return chemin_cercle(a("cx"), a("cy"), a("r"), a("r"))
    if balise == "ellipse":
        return chemin_cercle(a("cx"), a("cy"), a("rx"), a("ry"))
    if balise == "rect":
        return chemin_rect(a("x"), a("y"), a("width"), a("height"), a("rx", el.get("ry", 0)))
    if balise == "line":
        return f"M{nombre(a('x1'))},{nombre(a('y1'))} L{nombre(a('x2'))},{nombre(a('y2'))}"
    return None


def transformation(texte):
    """rotate et translate en attributs de groupe Android"""
    attributs = {}
    for fonction, args in re.findall(r"(rotate|translate)\(([^)]*)\)", texte or ""):
        valeurs = [float(v) for v in re.split(r"[\s,]+", args.strip())]
        if fonction == "rotate":
            attributs["android:rotation"] = nombre(valeurs[0])
            if len(valeurs) == 3:
                attributs["android:pivotX"] = nombre(valeurs[1])
                attributs["android:pivotY"] = nombre(valeurs[2])
        else:
            attributs["android:translateX"] = nombre(valeurs[0])
            attributs["android:translateY"] = nombre(valeurs[1] if len(valeurs) > 1 else 0)
    return attributs


def style(el, parent):
    """Style hérité : les attributs de présentation descendent, l'opacité se multiplie"""
    s = dict(parent)
    for nom in HERITES:
        if el.get(nom) is not None:
            s[nom] = el.get(nom)
    s["opacity"] = parent.get("opacity", 1.0) * float(el.get("opacity", 1))
    return s


def chemin_android(el, s, retrait):
    """Un android:path, ou rien si l'élément ne peint rien"""
    donnees = chemin(el)
    if donnees is None:
        return []
    balise = el.tag.removeprefix(SVG)
    attributs = {"android:pathData": donnees}
    remplissage = s.get("fill", "none" if balise == "line" else "#000000")
    if remplissage != "none":
        attributs["android:fillColor"] = remplissage
        if s["opacity"] < 1:
            attributs["android:fillAlpha"] = nombre(s["opacity"])
    trait = s.get("stroke", "none")
    if trait != "none":
        attributs["android:strokeColor"] = trait
        attributs["android:strokeWidth"] = nombre(s.get("stroke-width", 1))
        if s.get("stroke-linecap"):
            attributs["android:strokeLineCap"] = s["stroke-linecap"]
        if s.get("stroke-linejoin"):
            attributs["android:strokeLineJoin"] = s["stroke-linejoin"]
        if s["opacity"] < 1:
            attributs["android:strokeAlpha"] = nombre(s["opacity"])
    if len(attributs) == 1:
        return []
    return [ligne(retrait, "path", attributs)]


def ligne(retrait, balise, attributs, fermee=True):
    espaces = " " * retrait
    if not attributs:
        return f"{espaces}<{balise}{'/' if fermee else ''}>"
    interieur = f"\n{espaces}    ".join(f'{k}="{v}"' for k, v in attributs.items())
    return f"{espaces}<{balise}\n{espaces}    {interieur}{' />' if fermee else '>'}"


def convertir_enfants(el, s, clips, retrait):
    lignes = []
    for enfant in el:
        lignes += convertir(enfant, s, clips, retrait)
    return lignes


def convertir(el, parent, clips, retrait):
    """Un élément SVG en lignes XML Android"""
    balise = el.tag.removeprefix(SVG)
    if balise in ("defs", "clipPath", "title", "desc"):
        return []
    s = style(el, parent)
    groupe = transformation(el.get("transform"))
    clip = re.match(r"url\(#(.+)\)", el.get("clip-path", ""))
    if balise == "g" and not groupe and not clip:
        return convertir_enfants(el, s, clips, retrait)
    if balise == "g" or groupe or clip:
        lignes = [ligne(retrait, "group", groupe, fermee=False)]
        if clip:
            lignes.append(ligne(retrait + 4, "clip-path", {"android:pathData": clips[clip.group(1)]}))
        if balise == "g":
            lignes += convertir_enfants(el, s, clips, retrait + 4)
        else:
            lignes += chemin_android(el, s, retrait + 4)
        lignes.append(" " * retrait + "</group>")
        return lignes
    return chemin_android(el, s, retrait)


def vers_drawable(svg):
    racine = ET.parse(svg).getroot()
    _, _, largeur, hauteur = (float(v) for v in racine.get("viewBox").split())
    clips = {
        c.get("id"): " ".join(chemin(f) for f in c if chemin(f))
        for c in racine.iter(f"{SVG}clipPath")
    }
    entete = (f'<?xml version="1.0" encoding="utf-8"?>\n'
              f"<!-- Généré depuis design/illustrations/{svg.parent.name}/{svg.name} par vers_drawable.py -->\n"
              f'<vector xmlns:android="http://schemas.android.com/apk/res/android"\n'
              f'    android:width="{nombre(largeur)}dp"\n    android:height="{nombre(hauteur)}dp"\n'
              f'    android:viewportWidth="{nombre(largeur)}"\n    android:viewportHeight="{nombre(hauteur)}">')
    corps = convertir_enfants(racine, {"opacity": 1.0}, clips, 4)
    return "\n".join([entete, *corps, "</vector>", ""])


def main():
    sources = sorted(SOURCES.glob("*/*.svg"))
    if not sources:
        sys.exit(f"Aucun SVG dans {SOURCES}")
    for svg in sources:
        cible = SORTIE / f"illustration_{svg.stem.replace('-', '_')}.xml"
        cible.write_text(vers_drawable(svg), encoding="utf-8")
        print(f"{svg.relative_to(RACINE)} -> {cible.relative_to(RACINE)}")


if __name__ == "__main__":
    main()
