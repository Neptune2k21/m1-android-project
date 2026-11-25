package com.example.m1_android_project.core.testing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainDispatcherRuleTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `un viewModelScope passe du chargement au contenu`() = runTest {
        val source = mockk<Source>()
        coEvery { source.lire() } returns "contenu"

        val viewModel = ExempleViewModel(source)

        viewModel.etat.test {
            assertEquals("chargement", awaitItem())
            assertEquals("contenu", awaitItem())
        }
    }

    private interface Source {
        suspend fun lire(): String
    }

    private class ExempleViewModel(source: Source) : ViewModel() {
        val etat = MutableStateFlow("chargement")

        init {
            viewModelScope.launch { etat.value = source.lire() }
        }
    }
}
