package com.example.m1_android_project.core.model

import com.example.m1_android_project.core.testing.Fixtures
import org.junit.Assert.*
import org.junit.Test

class ModelesTest {
    @Test
    fun `recettes et boissons partagent le contrat de carte avec des identifiants distincts`() {
        val elements: List<ElementCarte> = listOf(Fixtures.recette, Fixtures.cocktailSansAlcool) + Fixtures.vins
        assertEquals(elements.size, elements.map { it.id }.toSet().size)
        assertTrue(elements.all { it.id.isNotBlank() && it.nom.isNotBlank() })
        assertEquals("mealdb:", Fixtures.recette.id.substringBefore(':') + ":")
        assertEquals("cocktaildb:", Fixtures.cocktailSansAlcool.id.substringBefore(':') + ":")
    }

    @Test
    fun `les portions et ingredients de base restent reutilisables`() {
        val recette = Fixtures.recette
        assertEquals(4, recette.portionsBase)
        assertEquals(listOf("Tomates cerises", "Sel"), recette.ingredients.map { it.nom })
        val pourSix = recette.ingredients.map { ingredient ->
            ingredient.copy(quantite = ingredient.quantite.mettreAEchelle(recette.portionsBase, 6))
        }
        assertEquals(lireQuantite("375g"), pourSix.first().quantite)
        assertEquals(Quantite.Libre("to taste"), pourSix.last().quantite)
        assertEquals(lireQuantite("250g"), recette.ingredients.first().quantite)
    }

    @Test
    fun `un ingredient sans mesure se distingue d'une quantite nulle`() {
        assertEquals(Quantite.NonRenseignee, Ingredient("Sel").quantite)
        assertNotEquals(Ingredient("Sel"), Ingredient("Sel", lireQuantite("0g")))
    }

    @Test
    fun `les informations facultatives peuvent etre inconnues`() {
        val recette = Recette("mealdb:test", "Exemple", ingredients = emptyList(), etapes = emptyList())
        assertNull(recette.photoUrl)
        assertNull(recette.pays)
        assertNull(recette.origineCulinaire)
        assertNull(recette.categorie)
        assertNull(recette.materiel)
        assertNull(recette.dureeMinutes)
        assertEquals(emptySet<Equipement>(), recette.copy(materiel = emptySet()).materiel)
        assertNull(Fixtures.cocktailSansAlcool.verre)
    }

    @Test
    fun `la collection de vins represente trois couleurs et des donnees locales`() {
        val vins = Fixtures.vins
        assertEquals(setOf(CouleurVin.Rouge, CouleurVin.Blanc, CouleurVin.Rose), vins.map { it.couleur }.toSet())
        assertTrue(vins.all { it.contientAlcool && it.id.startsWith("vin:") })
        assertTrue(vins.all { it.cepage.isNotBlank() && it.accords.isNotEmpty() })
        assertEquals(listOf(16, 10, 10), vins.map { it.temperatureServiceCelsius })
        assertTrue(vins.all { it.photoUrl == null })
        val boissons: List<Boisson> = listOf(Fixtures.cocktailSansAlcool) + vins
        assertFalse(boissons.first().contientAlcool)
    }

    @Test
    fun `le profil initial conserve une majorite non renseignee et le choix sans alcool`() {
        val profil = Profil()
        assertEquals(Majorite.NonRenseignee, profil.majorite)
        assertEquals(PreferenceBoissons.SansAlcool, profil.preferenceBoissons)
        assertTrue(profil.regimes.isEmpty())
        assertTrue(profil.equipements.isEmpty())
        assertNull(profil.prenom)
        assertNull(profil.avatar)
    }

    @Test
    fun `la majorite et les preferences restent independantes`() {
        assertEquals(Majorite.Mineur, Fixtures.profilMineur.majorite)
        assertEquals(Majorite.Majeur, Fixtures.profilMajeurSansAlcool.majorite)
        assertEquals(PreferenceBoissons.SansAlcool, Fixtures.profilMajeurSansAlcool.preferenceBoissons)
        assertEquals(PreferenceBoissons.VinsEtCocktails, Fixtures.profilMajeurAvecAlcool.preferenceBoissons)
        val profil = Profil(
            regimes = setOf(Regime.Vegetarien, Regime.SansGluten),
            equipements = setOf(Equipement.Four, Equipement.Mixeur),
        )
        assertEquals(2, profil.regimes.size)
        assertEquals(2, profil.equipements.size)
    }

    @Test
    fun `refuse les identites vides a la construction et lors des copies`() {
        assertThrows(IllegalArgumentException::class.java) { Ingredient(" ") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.recette.copy(id = "") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.recette.copy(nom = " ") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.cocktailSansAlcool.copy(id = " ") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.cocktailSansAlcool.copy(nom = "") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.vins.first().copy(id = "") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.vins.first().copy(nom = " ") }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.vins.first().copy(cepage = "") }
    }

    @Test
    fun `exige des identifiants avec source et valeur`() {
        listOf("123", "mealdb:", ":123", "mealdb: ", " mealdb:123", "mealdb:12 3").forEach { id ->
            assertThrows(id, IllegalArgumentException::class.java) { Fixtures.recette.copy(id = id) }
            assertThrows(id, IllegalArgumentException::class.java) { Fixtures.cocktailSansAlcool.copy(id = id) }
            assertThrows(id, IllegalArgumentException::class.java) { Fixtures.vins.first().copy(id = id) }
        }
    }

    @Test
    fun `refuse les portions et durees invalides`() {
        listOf(0, -1).forEach { portions ->
            assertThrows(IllegalArgumentException::class.java) { Fixtures.recette.copy(portionsBase = portions) }
        }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.recette.copy(dureeMinutes = -1) }
        assertEquals(0, Fixtures.recette.copy(dureeMinutes = 0).dureeMinutes)
    }

    @Test
    fun `les etapes conservent leur ordre et refusent des lignes blanches`() {
        val etapes = listOf("Laver les tomates.", "Assaisonner.")
        assertEquals(etapes, Fixtures.recette.copy(etapes = etapes).etapes)
        assertThrows(IllegalArgumentException::class.java) { Fixtures.recette.copy(etapes = listOf(" ")) }
        assertThrows(IllegalArgumentException::class.java) { Fixtures.cocktailSansAlcool.copy(etapes = listOf("")) }
    }
}
