package com.example.m1_android_project.core.network.cocktaildb

import com.example.m1_android_project.core.di.NetworkModule
import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Cocktail
import com.example.m1_android_project.core.model.Ingredient
import com.example.m1_android_project.core.model.Quantite
import com.example.m1_android_project.core.network.FauxServeur
import com.example.m1_android_project.core.network.ResultatApi
import com.example.m1_android_project.core.network.Sources
import com.example.m1_android_project.core.state.AppError
import java.math.BigDecimal
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class CocktailDbDataSourceTest {

    private val serveur = FauxServeur()
    private val source = CocktailDbDataSource(
        NetworkModule.provideCocktailApi(
            NetworkModule.provideCocktailDbRetrofit(serveur.client, NetworkModule.provideJson()),
        ),
    )

    @Test
    fun `lit une fiche complete`() = runTest {
        serveur.repondre("cocktaildb/lookup-11007.json")

        val cocktail = source.fiche("cocktaildb:11007").valeur()!!

        assertEquals(Sources.URL_COCKTAILDB + "lookup.php?i=11007", serveur.derniereUrl.toString())
        assertEquals("cocktaildb:11007", cocktail.id)
        assertEquals("Margarita", cocktail.nom)
        assertTrue(cocktail.contientAlcool)
        assertEquals("Cocktail glass", cocktail.verre)
        assertEquals("https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg", cocktail.photoUrl)
        assertEquals(
            listOf(
                Ingredient("Tequila", onces("1.5")),
                Ingredient("Triple sec", onces("0.5")),
                Ingredient("Lime juice", onces("1")),
                Ingredient("Salt"),
            ),
            cocktail.ingredients,
        )
        assertEquals(
            listOf(
                "Rub the rim of the glass with the lime slice to make the salt stick to it.",
                "Take care to moisten only the outer rim and sprinkle the salt on it.",
                "The salt should present to the lips of the imbiber and never mix into the cocktail.",
                "Shake the other ingredients with ice, then carefully pour into the glass.",
            ),
            cocktail.etapes,
        )
    }

    @Test
    fun `optional alcohol compte comme alcoolise`() = runTest {
        serveur.repondre("cocktaildb/lookup-12864.json")

        val cocktail = source.fiche("cocktaildb:12864").valeur()!!

        assertTrue(cocktail.contientAlcool)
        assertEquals(4, cocktail.etapes.size)
    }

    @Test
    fun `decoupe les lignes numerotees puis les phrases`() = runTest {
        serveur.repondre("cocktaildb/lookup-178370.json")

        val cocktail = source.fiche("cocktaildb:178370").valeur()!!

        assertEquals(
            listOf(
                "Pour the Ricard (or pastis)",
                "Pour the orgeat syrup",
                "Finally pour the water and add ice cubes at your convenience.",
                "Add the ice cubes at the end, otherwise the syrup and pastis do not mix well.",
            ),
            cocktail.etapes,
        )
        assertEquals(Quantite.Libre("Full Glass"), cocktail.ingredients.last().quantite)
    }

    @Test
    fun `filtre sans alcool en apercus`() = runTest {
        serveur.repondre("cocktaildb/filter-sans-alcool.json")

        val apercus = source.parAlcool(avecAlcool = false).valeur()

        assertEquals(Sources.URL_COCKTAILDB + "filter.php?a=Non_Alcoholic", serveur.derniereUrl.toString())
        assertEquals(58, apercus.size)
        assertEquals(
            Apercu("cocktaildb:12560", "Afterglow", "https://www.thecocktaildb.com/images/media/drink/vuquyv1468876052.jpg"),
            apercus.first(),
        )
    }

    @Test
    fun `filtre par verre avec le nom de la liste`() = runTest {
        serveur.repondre("cocktaildb/list-verres.json")
        val verres = source.verres().valeur()
        assertEquals(Sources.URL_COCKTAILDB + "list.php?g=list", serveur.derniereUrl.toString())
        assertEquals(32, verres.size)

        serveur.repondre("cocktaildb/filter-verre.json")
        val apercus = source.parVerre("Cocktail glass").valeur()
        assertEquals(Sources.URL_COCKTAILDB + "filter.php?g=Cocktail%20glass", serveur.derniereUrl.toString())
        assertEquals(100, apercus.size)
    }

    @Test
    fun `no data found et drinks null donnent une liste vide`() = runTest {
        serveur.repondre("cocktaildb/aucun.json")
        assertEquals(ResultatApi.Succes(emptyList<Apercu>()), source.parVerre("Atlantis glass"))

        serveur.repondre("cocktaildb/vide.json")
        assertEquals(ResultatApi.Succes(emptyList<Cocktail>()), source.chercher("zzz"))
        assertEquals(ResultatApi.Succes(null), source.fiche("cocktaildb:1"))
    }

    @Test
    fun `seul Non alcoholic est sans alcool`() {
        assertFalse(contientAlcool("Non alcoholic"))
        assertFalse(contientAlcool(" non  Alcoholic "))
        assertTrue(contientAlcool("Alcoholic"))
        assertTrue(contientAlcool("Optional alcohol"))
        assertTrue(contientAlcool(null))
    }

    @Test
    fun `traduit une erreur du serveur`() = runTest {
        serveur.repondreTexte("", code = 500)
        assertEquals(ResultatApi.Echec(AppError.Serveur), source.auHasard())
    }

    @Test
    fun `refuse un id d'une autre source`() {
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { source.fiche("mealdb:52777") }
        }
    }

    private fun onces(valeur: String) = Quantite.Numerique(BigDecimal(valeur), "oz")

    private fun <T> ResultatApi<T>.valeur(): T = (this as ResultatApi.Succes).valeur
}
