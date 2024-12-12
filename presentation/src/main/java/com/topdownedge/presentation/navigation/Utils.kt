package com.topdownedge.presentation.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController


fun <T : ScreenDestination> NavHostController.navigateClearingBackStack(destinationRoute: T) {
    this.navigate(destinationRoute) {
        launchSingleTop = true
        popUpTo(this@navigateClearingBackStack.graph.id) {
            inclusive = true
        }
    }
}

fun <T : ScreenDestination> NavHostController.navigateSingleTopTo(destinationRoute: T) {
    this.navigate(destinationRoute) {
        launchSingleTop = true
        restoreState = true

        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }

    }
}

fun <T : ScreenDestination> NavHostController.navigateDeeperTo(destinationRoute: T) {
    this.navigate(destinationRoute) {
        launchSingleTop = true
    }
}