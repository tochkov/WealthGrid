package com.topdownedge.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


fun <T : ScreenDestination> NavHostController.navigateClearingBackStack(destinationRoute: T) {
    this.navigate(destinationRoute) {
        popUpTo(this@navigateClearingBackStack.graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun <T : ScreenDestination> NavHostController.navigateSingleTopTo(destinationRoute: T, saveState: Boolean = true) {
    this.navigate(destinationRoute) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            this.saveState = saveState
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun <T : ScreenDestination> NavHostController.navigateDeeperTo(destinationRoute: T) {
    this.navigate(destinationRoute) {
        launchSingleTop = true
    }
}

val EnterFromBottomTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
    {
        slideInVertically(
            initialOffsetY = { it }, // Bottom to top
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(200))
    }

val ExitToBottomTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
    {
        slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        )
    }