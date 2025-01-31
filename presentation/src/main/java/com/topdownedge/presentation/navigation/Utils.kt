package com.topdownedge.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Probably not the best way to do this, but lost too much time trying to figure it out.
 * Works perfectly, the only problem - triggers on configuration changes.
 */
@Composable
fun ScreenVisibilityObserver(
    onScreenEnter: () -> Unit,
    onScreenExit: () -> Unit,
){
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> onScreenEnter()
                Lifecycle.Event.ON_STOP -> onScreenExit()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


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