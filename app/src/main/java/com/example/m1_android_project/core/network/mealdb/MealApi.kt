package com.example.m1_android_project.core.network.mealdb

import retrofit2.http.GET
import retrofit2.http.Query

/** Les filtres ne renvoient qu'un résumé la fiche passe par lookup */
interface MealApi {

    @GET("search.php")
    suspend fun chercher(@Query("s") nom: String): ReponseRepas

    @GET("lookup.php")
    suspend fun fiche(@Query("i") id: String): ReponseRepas

    @GET("random.php")
    suspend fun auHasard(): ReponseRepas

    @GET("filter.php")
    suspend fun parCategorie(@Query("c") categorie: String): ReponseResumes

    @GET("filter.php")
    suspend fun parPays(@Query("a") origineCulinaire: String): ReponseResumes

    @GET("filter.php")
    suspend fun parIngredient(@Query("i") ingredient: String): ReponseResumes

    @GET("categories.php")
    suspend fun categories(): ReponseCategories

    @GET("list.php?a=list")
    suspend fun pays(): ReponsePays
}
