package com.example.m1_android_project.core.network.mealdb

import com.example.m1_android_project.core.di.NetworkModule
import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Categorie
import com.example.m1_android_project.core.model.Ingredient
import com.example.m1_android_project.core.model.Pays
import com.example.m1_android_project.core.model.Quantite
import com.example.m1_android_project.core.model.Recette
import com.example.m1_android_project.core.network.FauxServeur
import com.example.m1_android_project.core.network.ResultatApi
import com.example.m1_android_project.core.network.Sources
import com.example.m1_android_project.core.state.AppError
import java.math.BigDecimal
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MealDbDataSourceTest {

    private val serveur = FauxServeur()
    private val source = MealDbDataSource(
        NetworkModule.provideMealApi(
            NetworkModule.provideMealDbRetrofit(serveur.client, NetworkModule.provideJson()),
        ),
    )

    @Test
    fun `lit une fiche complete`() = runTest {
        serveur.repondre("mealdb/lookup-52777.json")

        val recette = source.fiche("mealdb:52777").valeur()!!

        assertEquals(Sources.URL_MEALDB + "lookup.php?i=52777", serveur.derniereUrl.toString())
        assertEquals("mealdb:52777", recette.id)
        assertEquals("Mediterranean Pasta Salad", recette.nom)
        assertEquals("IT", recette.pays)
        assertEquals("Italian", recette.origineCulinaire)
        assertEquals("Seafood", recette.categorie)
        assertEquals("https://www.themealdb.com/images/media/meals/wvqpwt1468339226.jpg", recette.photoUrl)
        assertEquals(
            listOf(
                Ingredient("mozzarella balls", grammes(200)),
                Ingredient("baby plum tomatoes", grammes(250)),
                Ingredient("fresh basil", Quantite.Numerique(BigDecimal.ONE, "bunch")),
                Ingredient("farfalle", grammes(350)),
                Ingredient("extra virgin olive oil", Quantite.Numerique(BigDecimal(3), "tbsp")),
                Ingredient("Green Olives", grammes(40)),
                Ingredient("tuna", grammes(200)),
                Ingredient("salt", Quantite.Libre("to taste")),
                Ingredient("pepper", Quantite.Libre("to taste")),
            ),
            recette.ingredients,
        )
        assertEquals(8, recette.etapes.size)
        assertEquals("Bring a large saucepan of salted water to the boil", recette.etapes.first())
    }

    @Test
    fun `retire les etiquettes STEP et les cases vides`() = runTest {
        serveur.repondre("mealdb/lookup-53215.json")

        val recette = source.fiche("mealdb:53215").valeur()!!

        assertEquals("SA", recette.pays)
        assertEquals(8, recette.etapes.size)
        assertEquals(
            "First, pan fry the black pepper and garlic over a dry medium heat until fragrant.",
            recette.etapes.first(),
        )
        assertEquals("Serve with flatbread. Enjoy!", recette.etapes.last())
        assertEquals(
            listOf("Tomato", "Extra Virgin Olive Oil", "Egg", "Garlic Clove", "Black Pepper", "Salt"),
            recette.ingredients.map { it.nom },
        )
        assertEquals(Quantite.Numerique(BigDecimal(4)), recette.ingredients[2].quantite)
    }

    @Test
    fun `cherche par nom`() = runTest {
        serveur.repondre("mealdb/search-arrabiata.json")

        val recettes = source.chercher("Arrabiata").valeur()

        assertEquals(Sources.URL_MEALDB + "search.php?s=Arrabiata", serveur.derniereUrl.toString())
        assertEquals(listOf("mealdb:52771"), recettes.map(Recette::id))
    }

    @Test
    fun `meals null donne une liste vide ou aucune fiche`() = runTest {
        serveur.repondre("mealdb/vide.json")

        assertEquals(ResultatApi.Succes(emptyList<Recette>()), source.chercher("zzz"))
        assertEquals(ResultatApi.Succes(null), source.fiche("mealdb:0"))
        assertEquals(ResultatApi.Succes(emptyList<Apercu>()), source.parIngredient("zzz"))
    }

    @Test
    fun `filtre par pays en apercus`() = runTest {
        serveur.repondre("mealdb/filter-italian.json")

        val apercus = source.parPays("Italian").valeur()

        assertEquals(Sources.URL_MEALDB + "filter.php?a=Italian", serveur.derniereUrl.toString())
        assertEquals(21, apercus.size)
        assertEquals(
            Apercu("mealdb:52961", "Budino Di Ricotta", "https://www.themealdb.com/images/media/meals/1549542877.jpg"),
            apercus.first(),
        )
    }

    @Test
    fun `reconnait chaque pays de TheMealDB`() = runTest {
        serveur.repondre("mealdb/list-areas.json")

        val pays = source.pays().valeur()

        assertEquals(Sources.URL_MEALDB + "list.php?a=list", serveur.derniereUrl.toString())
        assertEquals(195, pays.size)
        assertEquals(emptyList<Pays>(), pays.filter { it.code == null })
        // Deux pays qui commencent pareil
        assertEquals(Pays("MA", "Maroc", "Moroccan"), pays.single { it.origineCulinaire == "Moroccan" })
        assertEquals(Pays("ML", "Mali", "Malian"), pays.single { it.origineCulinaire == "Malian" })
    }

    @Test
    fun `liste les categories`() = runTest {
        serveur.repondre("mealdb/categories.json")

        val categories = source.categories().valeur()

        assertEquals(14, categories.size)
        assertEquals(Categorie("Beef", "https://www.themealdb.com/images/category/beef.png"), categories.first())
    }

    @Test
    fun `ignore un element sans id ou sans nom`() = runTest {
        serveur.repondreTexte(
            """{"meals":[{"idMeal":null,"strMeal":"Sans id"},{"idMeal":"1","strMeal":" "},{"idMeal":"2","strMeal":"Gardé"}]}""",
        )

        assertEquals(listOf("mealdb:2"), source.parCategorie("Beef").valeur().map(Apercu::id))
    }

    @Test
    fun `garde un ingredient sans mesure et ignore une mesure sans ingredient`() = runTest {
        serveur.repondreTexte(
            """{"meals":[{"idMeal":"1","strMeal":"Test",
               "strIngredient1":" Salt ","strMeasure1":"",
               "strIngredient2":"","strMeasure2":"1 tsp",
               "strIngredient3":"Water","strMeasure3":null}]}""",
        )

        assertEquals(
            listOf(Ingredient("Salt"), Ingredient("Water")),
            source.auHasard().valeur()!!.ingredients,
        )
    }

    @Test
    fun `traduit les erreurs du serveur et du reseau`() = runTest {
        serveur.repondreTexte("", code = 500)
        assertEquals(ResultatApi.Echec(AppError.Serveur), source.categories())

        serveur.repondreTexte("""{"meals":[{"idMeal":""")
        assertEquals(ResultatApi.Echec(AppError.Serveur), source.fiche("mealdb:52777"))

        serveur.couper(UnknownHostException())
        assertEquals(ResultatApi.Echec(AppError.HorsLigne), source.chercher("Arrabiata"))
    }

    @Test
    fun `refuse un id d'une autre source`() {
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { source.fiche("cocktaildb:11007") }
        }
    }

    private fun grammes(valeur: Int) = Quantite.Numerique(BigDecimal(valeur), "g")

    private fun <T> ResultatApi<T>.valeur(): T = (this as ResultatApi.Succes).valeur
}
