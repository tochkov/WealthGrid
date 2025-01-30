package com.topdownedge.presentation.common

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun randomColor() = Color(
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256)
)