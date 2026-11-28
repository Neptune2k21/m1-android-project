package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

/** Option d'un choix unique : grande illustration, titre, texte et bouton radio ; le parent pose selectableGroup */
@Composable
fun OptionIllustree(
    illustration: Painter,
    titre: String,
    texte: String,
    choisie: Boolean,
    onChoix: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val couleurs = TableTheme.couleurs
    val espacements = TableTheme.espacements
    val forme = TableTheme.formes.tuile
    val interactions = remember { MutableInteractionSource() }
    val presse by interactions.collectIsPressedAsState()
    val echelle by animateFloatAsState(if (presse) 0.98f else 1f, label = "echelle")
    val epaisseur by animateDpAsState(if (choisie) EpaisseurChoisie else espacements.filet, label = "epaisseur")
    val bord by animateColorAsState(if (choisie) couleurs.action else couleurs.filet, Fondu, label = "bord")
    val saut by avancement(choisie, dureeMs = 340)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = echelle
                scaleY = echelle
            }
            .clip(forme)
            .background(couleurs.fond)
            .border(epaisseur, bord, forme)
            .selectable(
                selected = choisie,
                interactionSource = interactions,
                indication = null,
                role = Role.RadioButton,
                onClick = onChoix,
            )
            .padding(espacements.m),
        horizontalArrangement = Arrangement.spacedBy(espacements.m),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            illustration,
            contentDescription = null,
            modifier = Modifier
                .size(TailleIllustration)
                .graphicsLayer {
                    val p = cles(saut, 0f to 0f, 0.45f to 1f, 1f to 0f)
                    translationY = -HauteurSaut.toPx() * p
                },
        )
        Column(
            modifier = Modifier.weight(1f).padding(top = espacements.xs),
            verticalArrangement = Arrangement.spacedBy(espacements.xs),
        ) {
            Text(titre, style = TableTheme.typo.valeur, color = couleurs.texte)
            Text(texte, style = TableTheme.typo.secondaire, color = couleurs.texteSecondaire)
        }
        Radio(choisie, Modifier.padding(top = espacements.xs))
    }
}

/** Anneau et point qui grossit en rebondissant */
@Composable
private fun Radio(choisie: Boolean, modifier: Modifier) {
    val couleurs = TableTheme.couleurs
    val point by animateFloatAsState(
        if (choisie) 1f else 0f,
        spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
        label = "point",
    )
    val anneau by animateColorAsState(if (choisie) couleurs.action else couleurs.texteSecondaire, Fondu, label = "anneau")
    Box(
        modifier.size(TailleRadio).border(2.dp, anneau, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(TaillePoint)
                .graphicsLayer {
                    scaleX = point.coerceAtLeast(0f)
                    scaleY = point.coerceAtLeast(0f)
                }
                .background(couleurs.action, CircleShape),
        )
    }
}

private val Fondu = tween<Color>(200)
private val TailleIllustration = 88.dp
private val TailleRadio = 22.dp
private val TaillePoint = 10.dp
private val EpaisseurChoisie = 2.dp
private val HauteurSaut = 4.dp

@ThemePreviews
@Composable
private fun ApercuOptionIllustree() = Planche {
    var choix by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(TableTheme.espacements.s),
    ) {
        OptionIllustree(
            illustration = painterResource(R.drawable.illustration_boissons_vins_et_cocktails),
            titre = "Vins et cocktails",
            texte = "Accords vin, cocktails, et une alternative sans alcool à chaque fois.",
            choisie = choix == 0,
            onChoix = { choix = 0 },
        )
        OptionIllustree(
            illustration = painterResource(R.drawable.illustration_boissons_sans_alcool),
            titre = "Uniquement sans alcool",
            texte = "Mocktails, thés glacés, citronnades. Aucun alcool affiché nulle part.",
            choisie = choix == 1,
            onChoix = { choix = 1 },
        )
    }
}
