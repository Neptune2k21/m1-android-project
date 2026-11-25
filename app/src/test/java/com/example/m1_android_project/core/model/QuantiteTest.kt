package com.example.m1_android_project.core.model

import java.math.BigDecimal
import java.math.MathContext
import java.util.Locale
import org.junit.Assert.*
import org.junit.Test

class QuantiteTest {
    @Test
    fun `lit les mesures numeriques des API et les formats prevus`() {
        val exemples = listOf(
            Triple("250g", "250", "g"),
            Triple("1 pound", "1", "lb"),
            Triple("1/4 cup", "0.25", "cup"),
            Triple("1 1/2 cups", "1.5", "cup"),
            Triple("1 1/2 oz ", "1.5", "oz"),
            Triple(" 2 tsp ", "2", "tsp"),
            Triple("3 cloves", "3", "cloves"),
            Triple("1 tin ", "1", "tin"),
            Triple("6 leaves", "6", "leaves"),
            Triple("4,5 cl", "4.5", "cl"),
            Triple("4.5cl", "4.5", "cl"),
            Triple("½ cup", "0.5", "cup"),
            Triple("1½ cups", "1.5", "cup"),
            Triple("¾", "0.75", null),
            Triple("1/2", "0.5", null),
            Triple("0g", "0", "g"),
        )
        exemples.forEach { (texte, valeur, unite) ->
            assertEquals(texte, Quantite.Numerique(BigDecimal(valeur), unite), lireQuantite(texte))
        }
    }

    @Test
    fun `conserve integralement les textes libres ou invalides`() {
        listOf(
            "to taste", "une pincée", " sprinkling ", "Juice of 1 ",
            "2-3 oz", "2 - 3 oz", "1–2 cups", "2-4",
            "1/0", "1 1/0 cups", "-5g", "−½ cup", "1.2.3 g",
            "1 cup to taste", "1 2 cups", "250 g extra", "NaN", "½2 cup", "1½2 cups",
            "1 t", "1 T",
        ).forEach { texte ->
            assertEquals(texte, Quantite.Libre(texte), lireQuantite(texte))
        }
    }

    @Test
    fun `distingue absence et zero`() {
        listOf(null, "", "  ", "\t\n").forEach {
            assertEquals(Quantite.NonRenseignee, lireQuantite(it))
        }
        assertEquals(Quantite.Numerique(BigDecimal.ZERO), lireQuantite("0"))
    }

    @Test
    fun `normalise les alias a la construction et lors de la lecture`() {
        val alias = mapOf(
            "g" to "GRAMMES", "kg" to "kilograms", "mg" to "milligrammes",
            "ml" to "mL", "cl" to "centilitres", "dl" to "décilitres",
            "l" to "liters", "tsp" to "cuillères à café",
            "tbsp" to "c. à soupe", "cup" to "cups", "oz" to "ounces",
            "fl oz" to "fluid   ounces", "lb" to "lbs", "piece" to "pièces",
        )
        alias.forEach { (canonique, variante) ->
            val attendu = Quantite.Numerique(BigDecimal.ONE, canonique)
            assertEquals(attendu, Quantite.Numerique(BigDecimal.ONE, " $variante "))
            assertEquals(attendu, lireQuantite("1 $variante"))
        }
        assertEquals("cloves", Quantite.Numerique(BigDecimal.ONE, " CLOVES ").unite)
        assertNull(Quantite.Numerique(BigDecimal.ONE, " ").unite)
    }

    @Test
    fun `normalise independamment de la langue du telephone`() {
        val precedente = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            assertEquals("l", (lireQuantite("1 LITERS") as Quantite.Numerique).unite)
        } finally {
            Locale.setDefault(precedente)
        }
    }

    @Test
    fun `compare les valeurs decimales independamment de leur echelle`() {
        val entier = Quantite.Numerique(BigDecimal("1"), "cup")
        val decimal = Quantite.Numerique(BigDecimal("1.00"), "cups")
        assertEquals(entier, decimal)
        assertEquals(entier.hashCode(), decimal.hashCode())
    }

    @Test
    fun `refuse les valeurs negatives a la construction`() {
        assertThrows(IllegalArgumentException::class.java) {
            Quantite.Numerique(BigDecimal("-1"), "g")
        }
    }

    @Test
    fun `recalcule les portions depuis la base sans la modifier`() {
        val base = lireQuantite("250g")
        assertEquals(lireQuantite("375g"), base.mettreAEchelle(4, 6))
        assertEquals(lireQuantite("250g"), base.mettreAEchelle(4, 4))
        assertEquals(lireQuantite("187.5g"), base.mettreAEchelle(4, 3))
        assertEquals(lireQuantite("0.75 cup"), lireQuantite("1 1/2 cups").mettreAEchelle(4, 2))
        assertEquals(lireQuantite("1"), lireQuantite("½").mettreAEchelle(1, 2))
    }

    @Test
    fun `preserve une longue valeur quand les portions sont identiques`() {
        val base = lireQuantite("123456789012345678901234567890123456789 g")
        assertSame(base, base.mettreAEchelle(4, 4))
    }

    @Test
    fun `divise avec DECIMAL128 et evite un arrondi intermediaire du rapport`() {
        val tiers = BigDecimal.ONE.divide(BigDecimal("3"), MathContext.DECIMAL128)
        assertEquals(Quantite.Numerique(tiers), lireQuantite("1/3"))
        assertEquals(Quantite.Numerique(tiers), lireQuantite("1").mettreAEchelle(3, 1))
        assertEquals(lireQuantite("1"), lireQuantite("3").mettreAEchelle(3, 1))
    }

    @Test
    fun `garde les mesures libres et absentes lors du changement de portions`() {
        val texte = Quantite.Libre("au goût")
        assertSame(texte, texte.mettreAEchelle(4, 6))
        assertSame(Quantite.NonRenseignee, Quantite.NonRenseignee.mettreAEchelle(4, 6))
    }

    @Test
    fun `exige des portions positives pour toutes les variantes`() {
        val quantites = listOf(lireQuantite("250g"), Quantite.Libre("au goût"), Quantite.NonRenseignee)
        quantites.forEach { quantite ->
            listOf(0 to 1, -1 to 1, 1 to 0, 1 to -1).forEach { (initiales, demandees) ->
                assertThrows(IllegalArgumentException::class.java) {
                    quantite.mettreAEchelle(initiales, demandees)
                }
            }
        }
    }

    @Test
    fun `additionne les unites egales ou absentes sans modifier les operandes`() {
        val gauche = lireQuantite("250g") as Quantite.Numerique
        assertEquals(lireQuantite("375g"), gauche.additionnerSiCompatible(lireQuantite("125 grams") as Quantite.Numerique))
        assertEquals(lireQuantite("2 cups"), (lireQuantite("1 cup") as Quantite.Numerique)
            .additionnerSiCompatible(lireQuantite("1 cups") as Quantite.Numerique))
        assertEquals(lireQuantite("1"), (lireQuantite("½") as Quantite.Numerique)
            .additionnerSiCompatible(lireQuantite("½") as Quantite.Numerique))
        assertEquals(lireQuantite("250g"), gauche)
    }

    @Test
    fun `refuse de convertir ou fusionner des unites distinctes`() {
        listOf("g" to "kg", "oz" to "fl oz", "tsp" to "tbsp", null to "piece").forEach { (gauche, droite) ->
            assertNull(Quantite.Numerique(BigDecimal.ONE, gauche)
                .additionnerSiCompatible(Quantite.Numerique(BigDecimal.ONE, droite)))
        }
    }
}
