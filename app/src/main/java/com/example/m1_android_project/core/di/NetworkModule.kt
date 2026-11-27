package com.example.m1_android_project.core.di

import com.example.m1_android_project.core.network.Sources
import com.example.m1_android_project.core.network.cocktaildb.CocktailApi
import com.example.m1_android_project.core.network.mealdb.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MealDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CocktailDb

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DELAI_SECONDES = 15L

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().callTimeout(DELAI_SECONDES, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    @MealDb
    fun provideMealDbRetrofit(client: OkHttpClient, json: Json): Retrofit =
        retrofit(Sources.URL_MEALDB, client, json)

    @Provides
    @Singleton
    @CocktailDb
    fun provideCocktailDbRetrofit(client: OkHttpClient, json: Json): Retrofit =
        retrofit(Sources.URL_COCKTAILDB, client, json)

    @Provides
    @Singleton
    fun provideMealApi(@MealDb retrofit: Retrofit): MealApi = retrofit.create()

    @Provides
    @Singleton
    fun provideCocktailApi(@CocktailDb retrofit: Retrofit): CocktailApi = retrofit.create()

    private fun retrofit(url: String, client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
