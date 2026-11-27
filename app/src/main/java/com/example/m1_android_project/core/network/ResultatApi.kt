package com.example.m1_android_project.core.network

import com.example.m1_android_project.core.state.AppError
import java.io.IOException
import java.io.InterruptedIOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

/** Réponse d'un appel, l'exception déjà traduite en AppError */
sealed interface ResultatApi<out T> {
    data class Succes<out T>(val valeur: T) : ResultatApi<T>
    data class Echec(val erreur: AppError) : ResultatApi<Nothing>
}

/** Seul endroit où l'on attrape les erreurs réseau. Un bug du code, lui, remonte */
suspend fun <T> safeApiCall(appel: suspend () -> T): ResultatApi<T> =
    try {
        ResultatApi.Succes(appel())
    } catch (e: CancellationException) {
        throw e
    } catch (e: InterruptedIOException) {
        // Délai d'OkHttp dépassé 
        ResultatApi.Echec(AppError.DelaiDepasse)
    } catch (e: IOException) {
        ResultatApi.Echec(AppError.HorsLigne)
    } catch (e: HttpException) {
        ResultatApi.Echec(AppError.Serveur)
    } catch (e: SerializationException) {
        ResultatApi.Echec(AppError.Serveur)
    }
