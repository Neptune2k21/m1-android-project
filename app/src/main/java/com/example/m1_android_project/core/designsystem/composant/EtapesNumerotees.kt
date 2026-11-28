package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews
import kotlinx.coroutines.launch

/** Fil des étapes : la barre verte glisse sous l'étape active, les étapes passées gardent leur chiffre en vert */
@Composable
fun EtapesNumerotees(etapes: List<String>, active: Int, modifier: Modifier = Modifier) {
    val couleurs = TableTheme.couleurs
    val espacements = TableTheme.espacements
    val bords = remember(etapes.size) { mutableStateListOf(*Array(etapes.size) { Bord() }) }
    val debut = remember { Animatable(0f) }
    val largeur = remember { Animatable(0f) }
    val placee = remember { booleanArrayOf(false) }
    val cible = bords.getOrNull(active)

    // Première mesure : la barre se pose, ensuite elle glisse
    LaunchedEffect(cible) {
        if (cible == null || cible.largeur == 0f) return@LaunchedEffect
        if (!placee[0]) {
            placee[0] = true
            debut.snapTo(cible.debut)
            largeur.snapTo(cible.largeur)
        } else {
            launch { debut.animateTo(cible.debut, Glissement) }
            largeur.animateTo(cible.largeur, Glissement)
        }
    }

    val annonce = stringResource(R.string.etape_sur, active + 1, etapes.size, etapes[active])
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = annonce }
            .drawBehind {
                val filet = espacements.filet.toPx()
                val trait = EpaisseurTrait.toPx()
                drawLine(couleurs.filet, Offset(0f, size.height - filet / 2), Offset(size.width, size.height - filet / 2), filet)
                drawRoundRect(
                    color = couleurs.action,
                    topLeft = Offset(debut.value, size.height - trait),
                    size = Size(largeur.value, trait),
                    cornerRadius = CornerRadius(trait / 2),
                )
            }
            .padding(bottom = espacements.m),
        horizontalArrangement = Arrangement.spacedBy(espacements.l),
    ) {
        etapes.forEachIndexed { i, nom ->
            Row(
                modifier = Modifier.onPlaced {
                    val bord = Bord(it.positionInParent().x, it.size.width.toFloat())
                    if (bords[i] != bord) bords[i] = bord
                },
                horizontalArrangement = Arrangement.spacedBy(espacements.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    Romains[i],
                    style = TableTheme.typo.valeur,
                    color = if (i <= active) couleurs.action else couleurs.texteSecondaire.copy(alpha = 0.7f),
                )
                Text(
                    nom,
                    style = TableTheme.typo.libelle,
                    color = if (i == active) couleurs.texte else couleurs.texteSecondaire,
                )
            }
        }
    }
}

/** Position et largeur d'une étape dans la rangée */
private data class Bord(val debut: Float = 0f, val largeur: Float = 0f)

private val Glissement = tween<Float>(320, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f))
private val EpaisseurTrait = 3.dp
private val Romains = listOf("I", "II", "III", "IV", "V", "VI")

@ThemePreviews
@Composable
private fun ApercuEtapesNumerotees() = Planche {
    val etapes = listOf("Régime", "Matériel", "Boissons")
    etapes.indices.forEach { EtapesNumerotees(etapes, active = it) }
}
