package com.example.m1_android_project.core.network.cocktaildb

import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Cocktail
import com.example.m1_android_project.core.network.ResultatApi
import com.example.m1_android_project.core.network.safeApiCall
import javax.inject.Inject

/** TheCocktailDB en modèles de l'app ou une AppError Aucun résultat donne une liste vide ou null */
class CocktailDbDataSource @Inject constructor(private val api: CocktailApi) {

    suspend fun chercher(nom: String): ResultatApi<List<Cocktail>> =
        safeApiCall { api.chercher(nom).cocktails() }

    /** id au format cocktaildb:11007 */
    suspend fun fiche(id: String): ResultatApi<Cocktail?> {
        require(id.startsWith("$SOURCE_COCKTAILDB:")) { "$id n'est pas un cocktail TheCocktailDB" }
        return safeApiCall { api.fiche(id.substringAfter(':')).cocktails().firstOrNull() }
    }

    suspend fun auHasard(): ResultatApi<Cocktail?> =
        safeApiCall { api.auHasard().cocktails().firstOrNull() }

    /** Les ->Optional alcohol ne sortent dans aucun des deux */
    suspend fun parAlcool(avecAlcool: Boolean): ResultatApi<List<Apercu>> =
        safeApiCall { api.parAlcool(if (avecAlcool) "Alcoholic" else "Non_Alcoholic").apercus() }

    /** Nom tel que renvoyé par verres() ->Cocktail glass */
    suspend fun parVerre(verre: String): ResultatApi<List<Apercu>> =
        safeApiCall { api.parVerre(verre).apercus() }

    suspend fun verres(): ResultatApi<List<String>> =
        safeApiCall { api.verres().verres() }
}
