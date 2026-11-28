package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.icone.avancement
import com.example.m1_android_project.core.designsystem.icone.cles
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Tuile d'un choix multiple : illustration, titre, précision et pastille qui se coche */
@Composable
fun TuileChoix(
    illustration: Painter,
    titre: String,
    precision: String,
    cochee: Boolean,
    onCocheChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couleurs = TableTheme.couleurs
    val espacements = TableTheme.espacements
    val forme = TableTheme.formes.tuile
    val interactions = remember { MutableInteractionSource() }
    val presse by interactions.collectIsPressedAsState()
    val echelle by animateFloatAsState(if (presse) 0.97f else 1f, label = "echelle")
    val fond by animateColorAsState(if (cochee) couleurs.action else couleurs.fond, Fondu, label = "fond")
    val bord by animateColorAsState(if (cochee) couleurs.action else couleurs.filet, Fondu, label = "bord")
    val texte by animateColorAsState(if (cochee) couleurs.surAction else couleurs.texte, Fondu, label = "texte")
    val detail by animateColorAsState(
        if (cochee) couleurs.surAction.copy(alpha = 0.8f) else couleurs.texteSecondaire,
        Fondu,
        label = "detail",
    )
    val saut by avancement(cochee, dureeMs = 340)
    val trace by animateFloatAsState(if (cochee) 1f else 0f, tween(260, delayMillis = 80), label = "trace")

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = echelle
                scaleY = echelle
            }
            .clip(forme)
            .background(fond)
            .border(espacements.filet, bord, forme)
            .toggleable(
                value = cochee,
                interactionSource = interactions,
                indication = null,
                role = Role.Checkbox,
                onValueChange = onCocheChange,
            )
            .heightIn(min = HauteurMin)
            .padding(espacements.m),
        verticalArrangement = Arrangement.spacedBy(espacements.s),
    ) {
        Box(Modifier.fillMaxWidth()) {
            // Un petit saut quand on coche
            Image(
                illustration,
                contentDescription = null,
                modifier = Modifier
                    .size(TailleIllustration)
                    .graphicsLayer {
                        val p = cles(saut, 0f to 0f, 0.45f to 1f, 1f to 0f)
                        translationY = -HauteurSaut.toPx() * p
                        scaleX = 1f + 0.04f * p
                        scaleY = 1f + 0.04f * p
                    },
            )
            Pastille(cochee, apparition = { saut }, trace = { trace }, Modifier.align(Alignment.TopEnd))
        }
        Text(titre, style = TableTheme.typo.valeur, color = texte)
        Text(precision, style = TableTheme.typo.secondaire, color = detail)
    }
}

/** Rond vide au repos, pastille qui rebondit et coche qui se trace quand on coche */
@Composable
private fun Pastille(cochee: Boolean, apparition: () -> Float, trace: () -> Float, modifier: Modifier) {
    val couleurs = TableTheme.couleurs
    if (cochee) {
        Box(
            modifier
                .size(TaillePastille)
                .graphicsLayer {
                    val s = cles(apparition(), 0f to 0.4f, 0.7f to 1.12f, 1f to 1f)
                    scaleX = s
                    scaleY = s
                }
                .background(couleurs.surAction, CircleShape)
                .coche(couleurs.action, trace),
        )
    } else {
        Box(modifier.size(TaillePastille).border(1.5.dp, couleurs.texteSecondaire.copy(alpha = 0.45f), CircleShape))
    }
}

private val Fondu = tween<Color>(200)
private val TailleIllustration = 56.dp
private val TaillePastille = 24.dp
private val HauteurMin = 148.dp
private val HauteurSaut = 4.dp

@ThemePreviews
@Composable
private fun ApercuTuileChoix() = Planche {
    var cochees by remember { mutableStateOf(setOf(3, 4)) }
    val regimes = listOf(
        Triple(R.drawable.illustration_regime_de_tout, "De tout", "Sans restriction"),
        Triple(R.drawable.illustration_regime_vegetarien, "Végétarien", "Ni viande ni poisson"),
        Triple(R.drawable.illustration_regime_vegetalien, "Végétalien", "Aucun produit animal"),
        Triple(R.drawable.illustration_regime_pescetarien, "Pescétarien", "Poisson, pas de viande"),
        Triple(R.drawable.illustration_regime_sans_gluten, "Sans gluten", "Blé, orge, seigle"),
        Triple(R.drawable.illustration_regime_sans_lactose, "Sans lactose", "Lait et dérivés"),
    )
    regimes.chunked(2).forEachIndexed { ligne, paire ->
        Row(horizontalArrangement = Arrangement.spacedBy(TableTheme.espacements.s)) {
            paire.forEachIndexed { colonne, (dessin, titre, precision) ->
                val i = ligne * 2 + colonne
                TuileChoix(
                    illustration = painterResource(dessin),
                    titre = titre,
                    precision = precision,
                    cochee = i in cochees,
                    onCocheChange = { cochees = if (it) cochees + i else cochees - i },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
