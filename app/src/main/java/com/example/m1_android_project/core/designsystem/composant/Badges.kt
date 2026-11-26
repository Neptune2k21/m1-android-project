package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Degré, « Alcool », « Cocktail » : seul badge en bordeaux */
@Composable
fun BadgeAlcool(texte: String, modifier: Modifier = Modifier) =
    Badge(texte, TableTheme.couleurs.teinteAlcool, TableTheme.couleurs.alcool, modifier)

@Composable
fun BadgeSansAlcool(texte: String, modifier: Modifier = Modifier) =
    Badge(texte, TableTheme.couleurs.teinteAction, TableTheme.couleurs.action, modifier)

/** Recette compatible avec le profil, ou adaptable avec une substitution */
@Composable
fun BadgeAdapte(texte: String, modifier: Modifier = Modifier) =
    Badge(texte, TableTheme.couleurs.teinteAdapte, TableTheme.couleurs.adapte, modifier)

/** Appareil demandé par la recette */
@Composable
fun BadgeMateriel(texte: String, modifier: Modifier = Modifier) =
    Badge(texte, Color.Transparent, TableTheme.couleurs.texte, modifier, bord = TableTheme.couleurs.filet)

/** Durée ou repère posé sur une photo */
@Composable
fun BadgeInfo(texte: String, modifier: Modifier = Modifier) =
    Badge(texte, TableTheme.couleurs.accent, TableTheme.couleurs.surAccent, modifier)

@Composable
private fun Badge(
    texte: String,
    fond: Color,
    couleur: Color,
    modifier: Modifier,
    bord: Color? = null,
) {
    val forme = TableTheme.formes.badge

    Text(
        text = texte,
        modifier = modifier
            .background(fond, forme)
            .then(if (bord != null) Modifier.border(TableTheme.espacements.filet, bord, forme) else Modifier)
            .padding(horizontal = TableTheme.espacements.m, vertical = TableTheme.espacements.xs),
        color = couleur,
        style = TableTheme.typo.badge,
    )
}

@ThemePreviews
@Composable
private fun ApercuBadges() = Planche {
    val espace = Arrangement.spacedBy(TableTheme.espacements.s)

    Row(horizontalArrangement = espace) {
        BadgeAlcool("Alcool")
        BadgeAlcool("12,5 % vol.")
        BadgeAlcool("Cocktail")
    }
    Row(horizontalArrangement = espace) {
        BadgeSansAlcool("Sans alcool")
        BadgeAdapte("Adapté · sans gluten")
    }
    Row(horizontalArrangement = espace) {
        BadgeMateriel("Plaques")
        BadgeMateriel("Four")
        BadgeMateriel("Mixeur")
    }
    // Sur une photo de plat
    Box(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFF8C7A4E), TableTheme.formes.photo),
    ) {
        BadgeInfo("25 min · sans four", Modifier.padding(TableTheme.espacements.l))
    }
}
