package com.example.m1_android_project.core.designsystem.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

/** Aperçu en clair et en sombre sur le fond de l'application */
@Preview(name = "Clair", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "Sombre",
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF0A1410,
)
annotation class ThemePreviews
