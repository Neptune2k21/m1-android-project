package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Ingrédient qu'on coche quand on l'a déjà, sur la fiche ou dans les courses */
@Composable
fun LigneIngredient(
    nom: String,
    quantite: String,
    coche: Boolean,
    onCocheChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couleurs = TableTheme.couleurs
    // Cochée, la ligne s'efface sans être barrée : elle doit rester lisible
    val couleurTexte by animateColorAsState(
        targetValue = if (coche) couleurs.texteSecondaire else couleurs.texte,
        animationSpec = tween(DureeTrace),
        label = "couleurTexte",
    )

    LignePointillee(
        libelle = nom,
        modifier = modifier.toggleable(value = coche, role = Role.Checkbox, onValueChange = onCocheChange),
        couleurLibelle = couleurTexte,
        fin = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(TableTheme.espacements.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(quantite, color = couleurTexte, style = TableTheme.typo.chiffre)
                Case(coche)
            }
        },
    )
}

/** La coche se trace d'un trait */
@Composable
private fun Case(coche: Boolean) {
    val couleurs = TableTheme.couleurs
    val trace by animateFloatAsState(
        targetValue = if (coche) 1f else 0f,
        animationSpec = tween(DureeTrace),
        label = "trace",
    )
    val fond by animateColorAsState(
        targetValue = if (coche) couleurs.action else Color.Transparent,
        animationSpec = tween(DureeTrace),
        label = "fond",
    )
    val bord by animateColorAsState(
        targetValue = if (coche) couleurs.action else couleurs.texteSecondaire,
        animationSpec = tween(DureeTrace),
        label = "bord",
    )

    Box(
        Modifier
            .size(TailleCase)
            .background(fond, TableTheme.formes.case)
            .border(TableTheme.espacements.filetFort, bord, TableTheme.formes.case)
            .drawWithCache {
                val mesure = PathMeasure().apply {
                    setPath(
                        Path().apply {
                            moveTo(size.width * 0.28f, size.height * 0.52f)
                            lineTo(size.width * 0.44f, size.height * 0.68f)
                            lineTo(size.width * 0.73f, size.height * 0.36f)
                        },
                        false,
                    )
                }
                val trait = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                val portion = Path()

                onDrawBehind {
                    portion.reset()
                    mesure.getSegment(0f, mesure.length * trace, portion, true)
                    drawPath(portion, couleurs.surAction, style = trait)
                }
            },
    )
}

private const val DureeTrace = 220
private val TailleCase = 24.dp

@ThemePreviews
@Composable
private fun ApercuLigneIngredient() = Planche {
    var coches by remember { mutableStateOf(setOf("Sel", "Poivre")) }

    Column {
        listOf(
            "Farfalle sans gluten" to "350 g",
            "Tomates cerises allongées" to "250 g",
            "Basilic frais" to "1 botte",
            "Huile d'olive vierge extra" to "3 c. à s.",
            "Sel" to "au goût",
            "Poivre" to "au goût",
        ).forEach { (nom, quantite) ->
            LigneIngredient(
                nom = nom,
                quantite = quantite,
                coche = nom in coches,
                onCocheChange = { coches = if (it) coches + nom else coches - nom },
            )
        }
    }
}
