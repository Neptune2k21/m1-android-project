package com.example.m1_android_project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.espresso.Espresso
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Noms en camelCase : Android avant la 11 refuse les espaces dans les noms de méthode
class NavigationTest {

    @get:Rule
    val regle = createComposeRule()

    @Before
    fun lancer() {
        regle.setContent { TableTheme { TableApp() } }
    }

    @Test
    fun unOngletRetrouveSonDefilement() {
        onglet("Explorer").performClick()
        regle.onNode(hasScrollAction()).performScrollToNode(hasText("Élément 30"))

        onglet("Carte").performClick()
        onglet("Explorer").performClick()

        regle.onNodeWithText("Élément 30").assertIsDisplayed()
    }

    @Test
    fun leRetourSystemeFermeLaFiche() {
        regle.onNodeWithText("Ouvrir une fiche d'exemple").performClick()
        regle.onNodeWithText("Fiche mealdb:52777").assertIsDisplayed()
        onglet("Explorer").assertDoesNotExist()

        Espresso.pressBack()

        regle.onNodeWithText("Ouvrir une fiche d'exemple").assertIsDisplayed()
        onglet("Explorer").assertIsDisplayed()
    }

    @Test
    fun leBoutonRetourFermeLaFiche() {
        regle.onNodeWithText("Ouvrir une fiche d'exemple").performClick()
        regle.onNodeWithText("Retour").performClick()

        regle.onNodeWithText("Ouvrir une fiche d'exemple").assertIsDisplayed()
    }

    @Test
    fun toucherLOngletActifRemonteEnHaut() {
        onglet("Explorer").performClick()
        regle.onNode(hasScrollAction()).performScrollToNode(hasText("Élément 30"))

        onglet("Explorer").performClick()

        regle.onNodeWithText("Élément 1").assertIsDisplayed()
    }

    private fun onglet(nom: String) = regle.onNode(hasText(nom) and hasClickAction())
}
