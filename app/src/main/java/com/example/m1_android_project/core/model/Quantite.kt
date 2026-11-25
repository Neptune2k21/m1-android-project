package com.example.m1_android_project.core.model

import java.math.BigDecimal
import java.math.MathContext

sealed interface Quantite {
    /**
     * La factory normalise les unités et l'échelle décimale pour l'égalité.
     * La construction contrôlée protège aussi les invariants des copies.
     */
    @ConsistentCopyVisibility
    data class Numerique private constructor(
        val valeur: BigDecimal,
        val unite: String?,
    ) : Quantite {
        init {
            require(valeur.signum() >= 0) { "Une quantité doit être positive ou nulle." }
            require(valeur == valeur.stripTrailingZeros()) { "La valeur doit être canonique." }
            require(unite == normaliserUnite(unite)) { "L'unité doit être canonique." }
        }

        companion object {
            operator fun invoke(valeur: BigDecimal, unite: String? = null): Numerique =
                Numerique(valeur.stripTrailingZeros(), normaliserUnite(unite))
        }
    }

    data class Libre(val texte: String) : Quantite {
        init {
            require(texte.isNotBlank()) { "Une quantité libre doit contenir du texte." }
        }
    }

    data object NonRenseignee : Quantite
}

/** Repartir de la quantité de base à chaque changement du nombre de portions. */
fun Quantite.mettreAEchelle(portionsInitiales: Int, portionsDemandees: Int): Quantite {
    require(portionsInitiales > 0 && portionsDemandees > 0) {
        "Les nombres de portions doivent être strictement positifs."
    }
    if (portionsInitiales == portionsDemandees) return this
    return when (this) {
        is Quantite.Numerique -> Quantite.Numerique(
            valeur.multiply(BigDecimal(portionsDemandees))
                .divide(BigDecimal(portionsInitiales), MathContext.DECIMAL128),
            unite,
        )
        is Quantite.Libre, Quantite.NonRenseignee -> this
    }
}

/** L'appelant vérifie séparément que les deux quantités désignent le même ingrédient. */
fun Quantite.Numerique.additionnerSiCompatible(
    autre: Quantite.Numerique,
): Quantite.Numerique? =
    if (unite == autre.unite) Quantite.Numerique(valeur.add(autre.valeur), unite) else null
