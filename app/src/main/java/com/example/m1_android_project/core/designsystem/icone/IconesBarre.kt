package com.example.m1_android_project.core.designsystem.icone

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Ardoise du jour la craie écrit puis la puce agrume s'allume */
@Composable
fun IconeCarte(actif: Boolean, modifier: Modifier = Modifier) {
    val avancement = avancement(actif, dureeMs = 750)
    val c = couleursIcone(actif)

    Canvas(modifier.size(TailleIcone)) {
        val p = avancement.value
        grille {
            drawRoundRect(c.teinte, Offset(3.5f, 3.5f), Size(17f, 12.5f), CornerRadius(2f))
            drawRoundRect(c.trait, Offset(3.5f, 3.5f), Size(17f, 12.5f), CornerRadius(2f), style = Stroke(Trait, join = StrokeJoin.Round))
            trait(Pieds, c.trait)
            tracer(Craie, p.entre(0f, .43f, LinearOutSlowInEasing), c.trait)
            tracer(Mot, p.entre(.16f, .59f, LinearOutSlowInEasing), c.trait)
            scale(p.entre(.43f, 1f, Rebond), pivot = Puce) { drawCircle(c.pastille, 1.2f, Puce) }
        }
    }
}

/** Boussole tamponnée d'agrume */
@Composable
fun IconeExplorer(actif: Boolean, modifier: Modifier = Modifier) =
    IconeTampon(actif, Cadran, pivot = Offset(12f, 12f), modifier) { couleur ->
        drawCircle(couleur, 9f, Offset(12f, 12f), style = Stroke(Trait))
        trait(Aiguille, couleur)
    }

/** Le verre se remplit et trinque avec la fourchette */
@Composable
fun IconeAccords(actif: Boolean, modifier: Modifier = Modifier) {
    val avancement = avancement(actif, dureeMs = 620)
    val c = couleursIcone(actif)

    Canvas(modifier.size(TailleIcone)) {
        val p = avancement.value
        // Les deux se penchent l'un vers l'autre jusqu'à se toucher
        val penche = cles(p, 0f to 0f, .4f to 5f, .58f to 2f, .74f to 3.5f, 1f to 0f)
        grille {
            rotate(3f + penche, pivot = Offset(7.5f, 19f)) {
                scale(1f, p.entre(.16f, .89f), pivot = Offset(7.5f, 11.25f)) { drawPath(Jus, c.agrume) }
                trait(Verre, c.trait)
            }
            rotate(-3f - penche, pivot = Offset(16.5f, 19f)) { trait(Fourchette, c.trait) }
            if (p > 0f && p < 1f) {
                val eclat = cles(p, 0f to 0f, .3f to 0f, .45f to 1f, 1f to 0f)
                scale(cles(p, 0f to .4f, .3f to .4f, .45f to 1.1f, 1f to 1.25f), pivot = Offset(12f, 3f)) {
                    trait(Eclats, c.accent.copy(alpha = eclat))
                }
            }
        }
    }
}

/** La baguette sort du cabas et se dandine */
@Composable
fun IconeCourses(actif: Boolean, modifier: Modifier = Modifier) {
    val avancement = avancement(actif, dureeMs = 720)
    val c = couleursIcone(actif)

    Canvas(modifier.size(TailleIcone)) {
        val p = avancement.value
        grille {
            translate(6.4f, 8f) {
                rotate(-18f, pivot = Offset.Zero) {
                    val sortie = cles(p, 0f to 0f, .3f to -2.6f, .48f to -2f, .64f to -1.6f, .8f to -.6f, 1f to 0f)
                    val dandine = cles(p, 0f to 0f, .3f to 0f, .48f to -7f, .64f to 6f, .8f to -2f, 1f to 0f)
                    translate(0f, sortie) {
                        rotate(dandine, pivot = Offset(0f, 4.3f)) { baguette(c) }
                    }
                }
            }
            trait(Anse, c.trait)
            // Le cabas encaisse en se tassant
            val largeur = cles(p, 0f to 1f, .48f to 1f, .58f to 1.05f, .72f to .98f, 1f to 1f)
            val hauteur = cles(p, 0f to 1f, .48f to 1f, .58f to .9f, .72f to 1.03f, 1f to 1f)
            scale(largeur, hauteur, pivot = Offset(12f, 20.5f)) {
                drawPath(Cabas, c.plein)
                trait(Cabas, c.trait)
            }
        }
    }
}

/** Cœur tamponné d'agrume */
@Composable
fun IconeFavoris(actif: Boolean, modifier: Modifier = Modifier) =
    IconeTampon(actif, Coeur, pivot = Offset(12f, 12.5f), modifier) { couleur -> trait(Coeur, couleur) }

/** Le tampon se pose en biais puis se cale en décalé sous le trait */
@Composable
private fun IconeTampon(
    actif: Boolean,
    forme: Path,
    pivot: Offset,
    modifier: Modifier,
    dessin: DrawScope.(Color) -> Unit,
) {
    val avancement = avancement(actif, dureeMs = 420)
    val c = couleursIcone(actif)

    Canvas(modifier.size(TailleIcone)) {
        val p = avancement.value
        grille {
            translate(1.6f, 1.6f) {
                rotate(cles(p, 0f to -14f, .55f to 2f, 1f to 0f), pivot) {
                    scale(cles(p, 0f to 1.5f, .55f to .93f, 1f to 1f), pivot = pivot) {
                        drawPath(forme, c.accent, alpha = c.tampon * p.entre(0f, .55f))
                    }
                }
            }
            scale(cles(p, 0f to 1f, .4f to .86f, 1f to 1f), pivot = Offset(12f, 12f)) { dessin(c.trait) }
        }
    }
}

private fun DrawScope.baguette(c: CouleursIcone) {
    val coin = Offset(-1.35f, -4.3f)
    val taille = Size(2.7f, 8.6f)
    drawRoundRect(c.teinteAgrume, coin, taille, CornerRadius(1.35f))
    drawRoundRect(c.trait, coin, taille, CornerRadius(1.35f), style = Stroke(Trait))
    trait(Entailles, c.trait, largeur = 1.25f)
}

private val Puce = Offset(15.6f, 11.8f)
private val Pieds = chemin("M7.5 16L6 20.5M16.5 16L18 20.5")
private val Craie = Trace("M7 7.6q1.1-1.3 2.2 0t2.2 0t2.2 0t2.2 0")
private val Mot = Trace("M7 11.8H12.5")
private val Cadran = Path().apply { addOval(Rect(3f, 3f, 21f, 21f)) }
private val Aiguille = chemin("M15.5 8.5L13.27 13.27L8.5 15.5L10.73 10.73Z")
private val Verre = chemin("M4.6 5.5H10.4V8.5Q10.4 12 7.5 12Q4.6 12 4.6 8.5ZM7.5 12V19M5 19H10")
private val Jus = chemin("M5.35 7.6H9.65V8.5Q9.65 11.25 7.5 11.25Q5.35 11.25 5.35 8.5Z")
private val Fourchette = chemin("M16.5 19V11.2M14 5.5V9Q14 11.2 16.5 11.2Q19 11.2 19 9V5.5M16.5 5.5V11.2")
private val Eclats = chemin("M11.9 3.2V1.7M9.9 3.6L9.1 2.8M13.9 3.6L14.7 2.8")
private val Entailles = chemin("M-.6 -1.7L.6 -2.5M-.6 .8L.6 0")
private val Anse = chemin("M9.5 10.5V8.5Q9.5 5.5 12 5.5Q14.5 5.5 14.5 8.5V10.5")
private val Cabas = chemin("M5.5 10.5H18.5L17.6 19.3Q17.4 20.5 16.2 20.5H7.8Q6.6 20.5 6.4 19.3Z")
private val Coeur = chemin(
    "M12 20.5C12 20.5 3.5 15.3 3.5 9.3C3.5 6.6 5.6 4.5 8.2 4.5C9.8 4.5 11.2 5.3 12 6.6" +
        "C12.8 5.3 14.2 4.5 15.8 4.5C18.4 4.5 20.5 6.6 20.5 9.3C20.5 15.3 12 20.5 12 20.5Z",
)

@ThemePreviews
@Composable
private fun ApercuIconesBarre() = Planche {
    val icones: List<@Composable (Boolean, Modifier) -> Unit> = listOf(
        { a, m -> IconeCarte(a, m) },
        { a, m -> IconeExplorer(a, m) },
        { a, m -> IconeAccords(a, m) },
        { a, m -> IconeCourses(a, m) },
        { a, m -> IconeFavoris(a, m) },
    )
    // Toucher une icône l'allume ou l'éteint
    listOf(false, true).forEach { depart ->
        Row(horizontalArrangement = Arrangement.spacedBy(TableTheme.espacements.l)) {
            icones.forEach { icone ->
                var actif by remember { mutableStateOf(depart) }
                Column { icone(actif, Modifier.size(48.dp).clickable { actif = !actif }) }
            }
        }
    }
}
