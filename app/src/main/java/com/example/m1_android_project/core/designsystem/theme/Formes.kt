package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object Formes {
    /** Case à cocher. */
    val case = RoundedCornerShape(8.dp)

    /** Pays, filtre. */
    val puce = RoundedCornerShape(12.dp)

    /** Encart, vignette. */
    val carte = RoundedCornerShape(14.dp)

    /** Bouton, champ de recherche. */
    val bouton = RoundedCornerShape(18.dp)

    /** Tuile de choix, option illustrée. */
    val tuile = RoundedCornerShape(20.dp)

    val photo = RoundedCornerShape(24.dp)

    /** Contenu qui remonte sur la photo d'une fiche. */
    val feuille = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    val badge = CircleShape
}

internal val FormesMaterial = Shapes(
    extraSmall = Formes.case,
    small = Formes.puce,
    medium = Formes.carte,
    large = Formes.bouton,
    extraLarge = Formes.photo,
)
