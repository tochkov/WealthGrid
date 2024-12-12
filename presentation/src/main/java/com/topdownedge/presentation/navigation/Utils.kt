package com.topdownedge.presentation.navigation

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

fun <T : ScreenDestination> NavHostController.navigateSingleTopTo(destinationRoute: T) {
    this.navigate(destinationRoute) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
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