package com.topdownedge.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.topdownedge.presentation.market.CompaniesListScreen
import com.topdownedge.presentation.news.NewsDetailsScreen
import com.topdownedge.presentation.news.NewsListScreen
import com.topdownedge.presentation.portfolio.PortfolioScreen

@Composable
internal fun WealthGridHomeScreen() {

    val navController = rememberNavController()

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text("Top app bar")
//                }
//            )
//        },
        bottomBar = {
            HomeBottomNavigationBar(navController = navController)
        }

    ) { innerPadding ->
        HomeScreenNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )

    }
}


private enum class NavBarElement(
    val route: ScreenDestination,
    val text: String,
    val icon: ImageVector,
    val iconSelected: ImageVector
) {
    Markets(ScreenDestination.MarketsGraph, "Markets", Icons.Outlined.Home, Icons.Filled.Home),
    Portfolio(ScreenDestination.PortfolioGraph, "Portfolio", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle),
    News(ScreenDestination.NewsGraph, "News", Icons.Outlined.DateRange, Icons.Filled.DateRange)
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
                        contentDescription = item.text
                    )

                },
                label = { Text(item.text) },
                selected = selected,
                onClick = { navController.navigateSingleTopTo(item.route) }
            )
        }
    }

}

@Composable
private fun HomeScreenNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenDestination.MarketsGraph,
        modifier = modifier
    ) {
        navigation<ScreenDestination.MarketsGraph>(startDestination = ScreenDestination.Markets) {
            composable<ScreenDestination.Markets> {
                CompaniesListScreen()
            }
        }

        navigation<ScreenDestination.PortfolioGraph>(startDestination = ScreenDestination.Portfolio) {
            composable<ScreenDestination.Portfolio> {
                PortfolioScreen()
            }
        }

        navigation<ScreenDestination.NewsGraph>(startDestination = ScreenDestination.News) {
            composable<ScreenDestination.News> {
                // TODO - back press + nav bar issue potential fix starts with something like this probably:
//            BackHandler {
//                navController.navigateSingleTopTo(ScreenDestination.Markets)
//            }
                NewsListScreen(
                    onListItemClick = { newsId ->
                        navController.navigateDeeperTo(ScreenDestination.SingleNews(newsId))
                    }
                )
            }
            composable<ScreenDestination.SingleNews> { backstackEntry ->
                val singleNews: ScreenDestination.SingleNews = backstackEntry.toRoute()
                NewsDetailsScreen(singleNews.newsId)
            }
        }
    }

}

