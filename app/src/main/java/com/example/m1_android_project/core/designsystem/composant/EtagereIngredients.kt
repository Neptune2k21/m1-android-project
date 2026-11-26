package com.example.m1_android_project.core.designsystem.composant

import android.graphics.Bitmap
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.Size
import coil3.transform.Transformation
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews
import com.example.m1_android_project.core.network.Sources

/** Ce que l'étagère montre d'un ingrédient */
data class IngredientEtagere(
    val nom: String,
    val quantite: String,
    val image: String?,
    val coche: Boolean = false,
)

/** Ingrédients posés sur un filet, toucher l'un d'eux le soulève et le coche. Prend toute la largeur */
@Composable
fun EtagereIngredients(
    ingredients: List<IngredientEtagere>,
    onCocheChange: (index: Int, coche: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val espacements = TableTheme.espacements
    val couleurFilet = TableTheme.couleurs.filetFort

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val y = (MargeHaute + TaillePhoto).toPx()
                drawLine(couleurFilet, Offset(0f, y), Offset(size.width, y), espacements.filetFort.toPx())
            },
        contentPadding = PaddingValues(horizontal = espacements.marge),
        horizontalArrangement = Arrangement.spacedBy(espacements.s),
    ) {
        itemsIndexed(ingredients) { index, ingredient ->
            Vignette(ingredient, onCocheChange = { onCocheChange(index, it) })
        }
    }
}

@Composable
private fun Vignette(ingredient: IngredientEtagere, onCocheChange: (Boolean) -> Unit) {
    val couleurs = TableTheme.couleurs
    // Ressort vif : léger rebond en fin de course
    val levee by animateDpAsState(
        targetValue = if (ingredient.coche) Levee else 0.dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "levee",
    )
    val echelleCoche by animateFloatAsState(
        targetValue = if (ingredient.coche) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "echelleCoche",
    )

    Column(
        modifier = Modifier
            .width(LargeurVignette)
            .toggleable(
                value = ingredient.coche,
                interactionSource = null,
                indication = null,
                role = Role.Checkbox,
                onValueChange = onCocheChange,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(top = MargeHaute)
                .size(TaillePhoto)
                .offset { IntOffset(0, -levee.roundToPx()) },
            contentAlignment = Alignment.BottomCenter,
        ) {
            PhotoIngredient(ingredient.image)
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(TailleCoche)
                    .graphicsLayer {
                        scaleX = echelleCoche
                        scaleY = echelleCoche
                    }
                    .background(couleurs.action, CircleShape)
                    .coche(couleurs.surAction),
            )
        }
        Spacer(Modifier.height(TableTheme.espacements.s))
        Text(
            text = ingredient.nom,
            color = couleurs.texte,
            style = TableTheme.typo.libelle,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
        Text(
            text = ingredient.quantite,
            color = couleurs.texteSecondaire,
            style = TableTheme.typo.secondaire,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

/** Assiette vide tant que l'image n'est pas là */
@Composable
private fun PhotoIngredient(image: String?) {
    val contexte = LocalPlatformContext.current
    val requete = remember(image, contexte) {
        ImageRequest.Builder(contexte).data(image).transformations(RogneMarges).build()
    }
    var chargee by remember(image) { mutableStateOf(false) }

    if (!chargee) {
        Box(
            Modifier
                .size(TailleAssiette)
                .background(TableTheme.couleurs.fond, CircleShape)
                .border(TableTheme.espacements.filetFort, TableTheme.couleurs.texteSecondaire, CircleShape),
        )
    }
    AsyncImage(
        model = requete,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.BottomCenter,
        contentScale = ContentScale.Fit,
        onSuccess = { chargee = true },
    )
}

/** Les PNG de TheMealDB sont centrés avec des marges : on les coupe pour poser l'objet sur le filet */
private object RogneMarges : Transformation() {
    override val cacheKey = "rogne-marges"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val largeur = input.width
        val hauteur = input.height
        val pixels = IntArray(largeur * hauteur)
        input.getPixels(pixels, 0, largeur, 0, 0, largeur, hauteur)

        var gauche = largeur
        var haut = hauteur
        var droite = -1
        var bas = -1
        for (y in 0 until hauteur) {
            for (x in 0 until largeur) {
                if (pixels[y * largeur + x] ushr 24 > SeuilOpacite) {
                    if (x < gauche) gauche = x
                    if (x > droite) droite = x
                    if (y < haut) haut = y
                    if (y > bas) bas = y
                }
            }
        }
        if (droite < 0) return input
        return Bitmap.createBitmap(input, gauche, haut, droite - gauche + 1, bas - haut + 1)
    }
}

// Ombres et halos presque transparents ne comptent pas
private const val SeuilOpacite = 40

private val LargeurVignette = 80.dp
private val TaillePhoto = 64.dp
private val TailleAssiette = 48.dp
private val TailleCoche = 20.dp
private val Levee = 8.dp
// Place pour la levée et le rebond du ressort
private val MargeHaute = 12.dp

@ThemePreviews
@Composable
private fun ApercuEtagereIngredients() = TableTheme {
    var coches by remember { mutableStateOf(setOf(1)) }
    val ingredients = listOf(
        Triple("Farfalle", "350 g", "Farfalle"),
        Triple("Tomates cerises", "250 g", "Cherry Tomatoes"),
        Triple("Mozzarella", "200 g", "Mozzarella"),
        Triple("Thon", "200 g", "Tuna"),
        Triple("Basilic", "1 botte", "Basil"),
        Triple("Huile d'olive", "3 c. à s.", "Olive Oil"),
    ).mapIndexed { index, (nom, quantite, image) ->
        IngredientEtagere(
            nom = nom,
            quantite = quantite,
            image = Sources.imageIngredient(image),
            coche = index in coches,
        )
    }

    Column(Modifier.width(390.dp).padding(vertical = TableTheme.espacements.l)) {
        Text(
            text = "Sur l'étagère",
            modifier = Modifier.padding(horizontal = TableTheme.espacements.marge),
            style = TableTheme.typo.section,
        )
        EtagereIngredients(
            ingredients = ingredients,
            onCocheChange = { index, coche -> coches = if (coche) coches + index else coches - index },
        )
    }
}
