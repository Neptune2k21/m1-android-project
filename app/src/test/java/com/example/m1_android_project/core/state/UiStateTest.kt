package com.example.m1_android_project.core.state

import com.example.m1_android_project.core.model.Boisson
import com.example.m1_android_project.core.model.ElementCarte
import com.example.m1_android_project.core.model.Recette
import com.example.m1_android_project.core.testing.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class UiStateTest {
    @Test
    fun `le premier chargement est utilisable pour une fiche et une liste`() {
        val fiche: UiState<Recette> = UiState.Chargement
        val liste: UiState<List<Boisson>> = UiState.Chargement
        assertSame(fiche, liste)
    }

    @Test
    fun `le contenu actualise conserve la fiche sans erreur`() {
        val fiche = Fixtures.recette
        val etat = UiState.Contenu(fiche)
        assertSame(fiche, etat.donnees)
        assertNull(etat.erreur)
    }

    @Test
    fun `chaque erreur de rafraichissement peut accompagner une fiche en cache`() {
        val fiche = Fixtures.recette
        listOf(AppError.HorsLigne, AppError.DelaiDepasse, AppError.Serveur).forEach { erreur ->
            val etat = UiState.Contenu(fiche, erreur)
            assertSame(fiche, etat.donnees)
            assertEquals(erreur, etat.erreur)
            val actualise = etat.copy(erreur = null)
            assertSame(fiche, actualise.donnees)
            assertNull(actualise.erreur)
            assertEquals(erreur, etat.erreur)
        }
    }

    @Test
    fun `un echec sans donnees expose sa cause pour chaque type de contenu`() {
        listOf(AppError.HorsLigne, AppError.DelaiDepasse, AppError.Serveur).forEach { cause ->
            val erreur = UiState.Erreur(cause)
            val fiche: UiState<Recette> = erreur
            val liste: UiState<List<Boisson>> = erreur
            assertSame(fiche, liste)
            assertEquals(cause, erreur.cause)
        }
    }

    @Test
    fun `une recherche vide reste un contenu valide`() {
        val etat = UiState.Contenu(emptyList<Boisson>())
        assertEquals(emptyList<Boisson>(), etat.donnees)
        assertNull(etat.erreur)
    }

    @Test
    fun `les variantes permettent une lecture exhaustive de l'etat`() {
        val etat: UiState<ElementCarte> = UiState.Contenu(Fixtures.recette)
        val nom = when (etat) {
            UiState.Chargement -> "chargement"
            is UiState.Contenu -> etat.donnees.nom
            is UiState.Erreur -> "erreur"
        }
        assertEquals(Fixtures.recette.nom, nom)
    }
}
