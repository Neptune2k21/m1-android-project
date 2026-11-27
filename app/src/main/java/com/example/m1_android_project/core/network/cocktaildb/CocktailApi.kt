package com.example.m1_android_project.core.network.cocktaildb

import retrofit2.http.GET
import retrofit2.http.Query

/** Les filtres ne renvoient qu'un résumé la fiche passe par lookup */
interface CocktailApi {

    @GET("search.php")
    suspend fun chercher(@Query("s") nom: String): ReponseBoissons

    @GET("lookup.php")
    suspend fun fiche(@Query("i") id: String): ReponseBoissons

    @GET("random.php")
    suspend fun auHasard(): ReponseBoissons

    /** Alcoholic ou Non_Alcoholic */
    @GET("filter.php")
    suspend fun parAlcool(@Query("a") alcool: String): ReponseResumesBoissons

    @GET("filter.php")
    suspend fun parVerre(@Query("g") verre: String): ReponseResumesBoissons

    @GET("list.php?g=list")
    suspend fun verres(): ReponseVerres
}
