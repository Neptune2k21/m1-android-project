package com.example.m1_android_project.core.network

import com.example.m1_android_project.core.state.AppError
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class SafeApiCallTest {

    @Test
    fun `renvoie la valeur quand l'appel reussit`() = runTest {
        assertEquals(ResultatApi.Succes(42), safeApiCall { 42 })
    }

    @Test
    fun `traduit un delai depasse`() = runTest {
        assertEquals(echec(AppError.DelaiDepasse), safeApiCall { throw SocketTimeoutException() })
        // callTimeout d'OkHttp
        assertEquals(echec(AppError.DelaiDepasse), safeApiCall { throw InterruptedIOException("timeout") })
    }

    @Test
    fun `traduit une absence de reseau`() = runTest {
        assertEquals(echec(AppError.HorsLigne), safeApiCall { throw UnknownHostException() })
    }

    @Test
    fun `traduit une erreur HTTP et un JSON illisible`() = runTest {
        val http500 = HttpException(Response.error<Any>(500, "".toResponseBody()))
        assertEquals(echec(AppError.Serveur), safeApiCall { throw http500 })
        assertEquals(echec(AppError.Serveur), safeApiCall { throw SerializationException("JSON") })
    }

    @Test
    fun `laisse passer l'annulation et les bugs`() {
        assertThrows(CancellationException::class.java) {
            runBlocking { safeApiCall { throw CancellationException() } }
        }
        assertThrows(IllegalStateException::class.java) {
            runBlocking { safeApiCall { error("bug") } }
        }
    }

    private fun echec(erreur: AppError) = ResultatApi.Echec(erreur)
}
