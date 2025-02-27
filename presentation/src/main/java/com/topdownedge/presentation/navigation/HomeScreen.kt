package com.topdownedge.presentation.navigation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.topdownedge.presentation.R
import com.topdownedge.presentation.market.MarketsScreen
import com.topdownedge.presentation.news.NewsListScreen
import com.topdownedge.presentation.portfolio.PortfolioScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun WealthGridHomeScreen(
    masterNavController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val homeScreenNavController = rememberNavController()

    Scaffold(
        topBar = {
            HomeAppBar(
                navController = homeScreenNavController,
                masterNavController = masterNavController
            )
        },
        bottomBar = {
//            https://claude.ai/chat/a7eca2c9-b2a6-49e2-b05c-7e4578ddf855
            HomeBottomNavigationBar(navController = homeScreenNavController)
        }

    ) { innerPadding ->
        HomeScreenNavHost(
            homeScreenNavController = homeScreenNavController,
            masterNavController = masterNavController,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            modifier = Modifier.padding(innerPadding),
        )

    }
}


private enum class NavBarElement(
    val route: ScreenDestination,
    @StringRes val textResource: Int,
    val icon: ImageVector,
    val iconSelected: ImageVector
) {
    Markets(ScreenDestination.Markets, R.string.home_tab_markets, Icons.Outlined.Home, Icons.Filled.Home),
    Portfolio(ScreenDestination.Portfolio, R.string.home_tab_portfolio, Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle),
    News(ScreenDestination.News, R.string.home_tab_news, Icons.Outlined.DateRange, Icons.Filled.DateRange)
}

@SuppressLint("RestrictedApi") // - https://issuetracker.google.com/issues/372175033
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeAppBar(
    navController: NavHostController,
    masterNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination

    TopAppBar(
        title = {
            val currentElement = NavBarElement.entries.firstOrNull { item ->
                currentDestination?.hierarchy?.any {
                    it.hasRoute(item.route::class)
                } == true
            }
            Text(
                text = currentElement?.let {
                    stringResource(it.textResource)
                } ?: ""
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,  // Same as BottomNavigation
        ),
        actions = {
            val currentElement = NavBarElement.entries.firstOrNull { item ->
                currentDestination?.hierarchy?.any {
                    it.hasRoute(item.route::class)
                } == true
            }
            when (currentElement) {
                NavBarElement.Markets -> {
                    IconButton(
                        onClick = {
                            masterNavController.navigateToSearchAssetScreen(isForNewTrade = false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(currentElement.textResource)
                        )
                    }
                }

                NavBarElement.Portfolio -> {
                    IconButton(
                        onClick = {
                            masterNavController.navigateToSearchAssetScreen(isForNewTrade = true)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(currentElement.textResource)
                        )
                    }
                }

                NavBarElement.News -> {

                }

                else -> {}

            }
        },
        modifier = modifier
    )
}


@SuppressLint("RestrictedApi") // - https://issuetracker.google.com/issues/372175033
@Composable
private fun HomeBottomNavigationBar(
    navController: NavHostController
) {
    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination

//    https://developer.android.com/develop/ui/compose/navigation#bottom-nav
//    TODO - check official and see why on back pressed loses state on tabs

    NavigationBar {
        NavBarElement.entries.forEachIndexed { index, item ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selected) item.iconSelected else item.icon,
                        contentDescription = stringResource(item.textResource)
                    )

                },
                label = { Text(stringResource(item.textResource)) },
                selected = selected,
                onClick = { navController.navigateSingleTopTo(item.route) }
            )
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun HomeScreenNavHost(
    homeScreenNavController: NavHostController,
    masterNavController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier,
) {
    NavHost(
        navController = homeScreenNavController,
        startDestination = ScreenDestination.Markets,
        modifier = modifier
    ) {
        composable<ScreenDestination.Markets> {
            MarketsScreen(masterNavController,
                sharedTransitionScope,
                animatedContentScope)
        }
        composable<ScreenDestination.Portfolio> {
            PortfolioScreen(masterNavController)
        }
        composable<ScreenDestination.News> {
            // TODO - back press + nav bar issue potential fix starts with something like this probably:
//            BackHandler {
//                navController.navigateSingleTopTo(ScreenDestination.Markets)
//            }
            NewsListScreen(masterNavController)
        }

    }

}

