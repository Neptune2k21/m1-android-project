package com.example.m1_android_project.core.model

import java.math.BigDecimal
import java.math.MathContext

private const val DECIMAL = "(?:[0-9]+(?:[.,][0-9]+)?|[.,][0-9]+)"
private const val FRACTION = "[0-9]+/[0-9]+"
private val mesure = Regex("^([0-9]+ +$FRACTION|$FRACTION|$DECIMAL) *(.*)$")
private val fraction = Regex("^([0-9]+)/([0-9]+)$")
private val uniteSimple = Regex("^\\p{L}+(?:-\\p{L}+)*$")
private val fractionsUnicode = mapOf(
    '¼' to "1/4", '½' to "1/2", '¾' to "3/4",
    '⅐' to "1/7", '⅑' to "1/9", '⅒' to "1/10",
    '⅓' to "1/3", '⅔' to "2/3", '⅕' to "1/5",
    '⅖' to "2/5", '⅗' to "3/5", '⅘' to "4/5",
    '⅙' to "1/6", '⅚' to "5/6", '⅛' to "1/8",
    '⅜' to "3/8", '⅝' to "5/8", '⅞' to "7/8",
)

/** Lit une mesure complète et conserve le texte brut si son sens est ambigu. */
fun lireQuantite(texte: String?): Quantite {
    if (texte.isNullOrBlank()) return Quantite.NonRenseignee
    if (texte.zipWithNext().any { (avant, apres) ->
            avant in fractionsUnicode && apres.isDigit()
        }
    ) return Quantite.Libre(texte)
    val normalise = normaliserEspaces(buildString {
        texte.forEach { caractere ->
            val remplacement = fractionsUnicode[caractere]
            if (remplacement == null) append(caractere) else append(" $remplacement")
        }
    })
    val resultat = mesure.matchEntire(normalise) ?: return Quantite.Libre(texte)
    val valeur = lireValeur(resultat.groupValues[1]) ?: return Quantite.Libre(texte)
    val unite = normaliserUnite(resultat.groupValues[2])
    // Les unités inconnues simples (cloves, tin...) sont conservées.
    // Un texte plus complexe demande une interprétation que le parseur ne peut garantir.
    if (unite != null && !estUniteConnue(unite) && !uniteSimple.matches(unite)) {
        return Quantite.Libre(texte)
    }
    return Quantite.Numerique(valeur, unite)
}

private fun lireValeur(texte: String): BigDecimal? {
    val parties = texte.split(' ', limit = 2)
    if (parties.size == 2) {
        val entier = parties[0].toBigDecimalOrNull() ?: return null
        val reste = lireValeur(parties[1]) ?: return null
        return entier.add(reste)
    }
    val resultat = fraction.matchEntire(texte)
    if (resultat != null) {
        val numerateur = resultat.groupValues[1].toBigDecimal()
        val denominateur = resultat.groupValues[2].toBigDecimal()
        if (denominateur.signum() == 0) return null
        return numerateur.divide(denominateur, MathContext.DECIMAL128)
    }
    return texte.replace(',', '.').toBigDecimalOrNull()
}
