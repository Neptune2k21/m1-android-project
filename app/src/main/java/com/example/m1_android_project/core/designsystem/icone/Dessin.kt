package com.example.m1_android_project.core.designsystem.icone

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.TableTheme

//icônes sont dessinées sur une grille de 24

internal val TailleIcone = 24.dp
private const val Grille = 24f
internal const val Trait = 1.75f

/** Dépassement en fin de course pour les pastilles qui apparaissent */
internal val Rebond = CubicBezierEasing(.3f, 1.6f, .5f, 1f)

internal fun chemin(d: String): Path = PathParser().parsePathString(d).toPath()

internal inline fun DrawScope.grille(dessin: DrawScope.() -> Unit) =
    scale(size.minDimension / Grille, pivot = Offset.Zero, block = dessin)

internal fun DrawScope.trait(chemin: Path, couleur: Color, largeur: Float = Trait) =
    drawPath(chemin, couleur, style = Stroke(largeur, cap = StrokeCap.Round, join = StrokeJoin.Round))

/** Chemin tracé en partie */
internal class Trace(d: String) {
    val chemin = chemin(d)
    private val mesure = PathMeasure().apply { setPath(chemin, false) }
    private val morceau = Path()

    fun partie(avancement: Float): Path {
        morceau.reset()
        mesure.getSegment(0f, mesure.length * avancement, morceau, true)
        return morceau
    }
}

internal fun DrawScope.tracer(trace: Trace, avancement: Float, couleur: Color) {
    if (avancement <= 0f) return
    trait(if (avancement >= 1f) trace.chemin else trace.partie(avancement), couleur)
}

/** Avancement de 0 à 1 d'une partie de l'animation */
internal fun Float.entre(debut: Float, fin: Float, easing: Easing = FastOutSlowInEasing): Float =
    easing.transform(((this - debut) / (fin - debut)).coerceIn(0f, 1f))

/** Valeur à l'instant p d'une suite d'images clés (instant, valeur) */
internal fun cles(p: Float, vararg images: Pair<Float, Float>): Float {
    val i = images.indexOfLast { it.first <= p }.coerceIn(0, images.size - 2)
    val (t0, v0) = images[i]
    val (t1, v1) = images[i + 1]
    return v0 + (v1 - v0) * p.entre(t0, t1)
}

/** Joue l'animation à chaque fois que l'icône s'allume pas à son premier affichage */
@Composable
internal fun avancement(actif: Boolean, dureeMs: Int): State<Float> {
    val avancement = remember { Animatable(1f) }
    val premier = remember { booleanArrayOf(true) }
    LaunchedEffect(actif) {
        if (premier[0]) {
            premier[0] = false
            return@LaunchedEffect
        }
        if (actif) {
            avancement.snapTo(0f)
            avancement.animateTo(1f, tween(dureeMs, easing = LinearEasing))
        }
    }
    return avancement.asState()
}

/** Couleurs d'une icône qui glissent entre repos et actif */
internal class CouleursIcone(
    val trait: Color,
    val accent: Color,
    /** Détails agrume, visibles une fois actif */
    val agrume: Color,
    val pastille: Color,
    val teinte: Color,
    val teinteAgrume: Color,
    /** Remplissage qui masque ce qui passe derrière */
    val plein: Color,
    val tampon: Float,
)

@Composable
internal fun couleursIcone(actif: Boolean): CouleursIcone {
    val couleurs = TableTheme.couleurs
    val trait by animateColorAsState(if (actif) couleurs.texte else couleurs.texteSecondaire, tween(200), label = "trait")
    val apparition by animateFloatAsState(if (actif) 1f else 0f, tween(200), label = "apparition")
    // L'agrume plein éblouit sur fond sombre
    val tampon = if (couleurs.fond.luminance() < .5f) .8f else 1f

    return CouleursIcone(
        trait = trait,
        accent = couleurs.accent,
        agrume = couleurs.accent.copy(alpha = apparition),
        pastille = lerp(trait, couleurs.accent, apparition),
        teinte = couleurs.teinteAction.copy(alpha = apparition),
        teinteAgrume = couleurs.teinteAccent.copy(alpha = apparition),
        plein = lerp(couleurs.fond, couleurs.teinteAction, apparition),
        tampon = tampon * apparition,
    )
}
