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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.topdownedge.presentation.market.CompaniesListScreen
import com.topdownedge.presentation.news.NewsDetailsScreen
import com.topdownedge.presentation.news.NewsListScreen
import com.topdownedge.presentation.portfolio.PortfolioScreen

@Composable
fun WealthGridHomeScreen() {

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
    Markets(
        ScreenDestination.Markets,
        "Markets",
        Icons.Outlined.Home,
        Icons.Filled.Home
    ),
    Portfolio(
        ScreenDestination.Portfolio,
        "Portfolio",
        Icons.Outlined.AccountCircle,
        Icons.Filled.AccountCircle
    ),
    News(
        ScreenDestination.News,
        "News",
        Icons.Outlined.DateRange,
        Icons.Filled.DateRange
    )
}


@SuppressLint("RestrictedApi") // - https://issuetracker.google.com/issues/372175033
@Composable
fun HomeBottomNavigationBar(
    navController: NavHostController
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination


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
internal fun HomeScreenNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenDestination.Markets,
        modifier = modifier
    ) {
        composable<ScreenDestination.Markets> {
            CompaniesListScreen()
        }
        composable<ScreenDestination.Portfolio> {
            PortfolioScreen()
        }
        composable<ScreenDestination.News> {
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

