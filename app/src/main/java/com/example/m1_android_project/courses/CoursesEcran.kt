package com.example.m1_android_project.courses

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.m1_android_project.R
import com.example.m1_android_project.core.ui.EcranAVenir

@Composable
fun CoursesEcran(modifier: Modifier = Modifier) {
    EcranAVenir(titre = stringResource(R.string.onglet_courses), modifier = modifier)
}
