package com.example.m1_android_project.core.network.mealdb

import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Categorie
import com.example.m1_android_project.core.model.Pays
import com.example.m1_android_project.core.model.Recette
import com.example.m1_android_project.core.network.ResultatApi
import com.example.m1_android_project.core.network.safeApiCall
import javax.inject.Inject

/** TheMealDB en modèles de l'app, ou une AppError. Aucun résultat donne une liste vide ou null */
class MealDbDataSource @Inject constructor(private val api: MealApi) {

    suspend fun chercher(nom: String): ResultatApi<List<Recette>> =
        safeApiCall { api.chercher(nom).recettes() }

    /** id au format mealdb:52777 */
    suspend fun fiche(id: String): ResultatApi<Recette?> {
        require(id.startsWith("$SOURCE_MEALDB:")) { "$id n'est pas une recette TheMealDB" }
        return safeApiCall { api.fiche(id.substringAfter(':')).recettes().firstOrNull() }
    }

    suspend fun auHasard(): ResultatApi<Recette?> =
        safeApiCall { api.auHasard().recettes().firstOrNull() }

    suspend fun parCategorie(categorie: String): ResultatApi<List<Apercu>> =
        safeApiCall { api.parCategorie(categorie).apercus() }

    suspend fun parPays(origineCulinaire: String): ResultatApi<List<Apercu>> =
        safeApiCall { api.parPays(origineCulinaire).apercus() }

    suspend fun parIngredient(ingredient: String): ResultatApi<List<Apercu>> =
        safeApiCall { api.parIngredient(ingredient).apercus() }

    suspend fun categories(): ResultatApi<List<Categorie>> =
        safeApiCall { api.categories().categories() }

    suspend fun pays(): ResultatApi<List<Pays>> =
        safeApiCall { api.pays().pays() }
}
