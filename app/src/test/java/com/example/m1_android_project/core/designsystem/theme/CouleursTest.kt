package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertTrue
import org.junit.Test

class CouleursTest {

    @Test
    fun `les textes restent lisibles en clair`() = verifierContrastes(CouleursClaires)

    @Test
    fun `les textes restent lisibles en sombre`() = verifierContrastes(CouleursSombres)

    private fun verifierContrastes(c: Couleurs) {
        val couples = mapOf(
            "texte sur fond" to (c.texte to c.fond),
            "texte sur surface" to (c.texte to c.surface),
            "texte sur teinteAction" to (c.texte to c.teinteAction),
            "texte sur teinteAccent" to (c.texte to c.teinteAccent),
            "texteSecondaire sur fond" to (c.texteSecondaire to c.fond),
            "texteSecondaire sur surface" to (c.texteSecondaire to c.surface),
            "action sur fond" to (c.action to c.fond),
            "action sur surface" to (c.action to c.surface),
            "action sur teinteAction" to (c.action to c.teinteAction),
            "surAction sur action" to (c.surAction to c.action),
            "surAccent sur accent" to (c.surAccent to c.accent),
            "adapte sur fond" to (c.adapte to c.fond),
            "adapte sur teinteAdapte" to (c.adapte to c.teinteAdapte),
            "alcool sur fond" to (c.alcool to c.fond),
            "alcool sur teinteAlcool" to (c.alcool to c.teinteAlcool),
        )

        couples.forEach { (nom, couple) ->
            val contraste = contraste(couple.first, couple.second)
            assertTrue("$nom : $contraste < $CONTRASTE_AA", contraste >= CONTRASTE_AA)
        }
    }

    private fun contraste(a: Color, b: Color): Float {
        val (sombre, clair) = listOf(a.luminance(), b.luminance()).sorted()
        return (clair + 0.05f) / (sombre + 0.05f)
    }

    private companion object {
        /** WCAG AA, texte courant. */
        const val CONTRASTE_AA = 4.5f
    }
}
