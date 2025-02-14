package com.topdownedge.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.NewsCategory
import com.topdownedge.presentation.common.applyAlpha
import com.topdownedge.presentation.navigation.navigateToNewsDetailsScreen
import com.topdownedge.presentation.ui.theme.ColorMaster

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsListScreen(
    masterNavController: NavHostController,
) {
    val viewModel: NewsListViewModel = hiltViewModel()
    val uiState = viewModel.newsState.collectAsStateWithLifecycle().value
    val scrollState = rememberLazyListState()
    val PRELOAD_TRESHOLD = 5

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refreshCategory() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    val categories = mutableListOf(
                        NewsCategory.General,
                        NewsCategory.Ticker("SBUX"),
                        NewsCategory.Ticker("AAPL"),
                        NewsCategory.Ticker("NVDA"),
                        NewsCategory.Topic("initial public offering"), // !
                        NewsCategory.Topic("earnings release"), // !
                        NewsCategory.Topic("financial results"), // !
                    )
                    items(categories.size) { i ->
                        val isSelected = categories[i] == uiState.selectedCategory
                        Button(
                            onClick = {
                                viewModel.onCategoryChange(categories[i])
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = categories[i].title,
//                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            items(uiState.news.size) { position ->
                LaunchedEffect(scrollState) {
                    if (position >= uiState.news.size - 1 - PRELOAD_TRESHOLD
                        && uiState.hasMoreToLoad
                        && !uiState.isLoading
                    ) {
                        viewModel.loadNextPage()
                    }
                }
                NewsItemCard2(
                    newsArticle = uiState.news[position],
                    onCardClick = {
                        masterNavController.navigateToNewsDetailsScreen(uiState.news[position])
                    }
                )
            }

            item {
                if (uiState.paginationError) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMassage)
                        Button(
                            onClick = {
                                viewModel.loadNextPage()
                            }
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
            item {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

    }
}

//@Preview(showBackground = true)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsItemCard2(
    newsArticle: NewsArticle,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.padding(horizontal = 11.dp, vertical = 8.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        ) {
            Text(
//                text = "NewsHeading Ashweaganda, Ashkolsun mashala Starwars The Gateman of the Galaxy",
                text = newsArticle.title,
                fontSize = 20.sp,
//                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge,

                )

            val uniqueSymbols = newsArticle.symbols.map { it.split(".")[0] }.distinct()

            FlowRow(
                maxLines = 1
            ) {
                uniqueSymbols.forEach { tag ->
                    Box(modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)) {


                        Text(
                            modifier = Modifier
                                .background(
                                    color = ColorMaster.primary.applyAlpha(0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 5.dp, vertical = 4.dp),
                            text = tag,
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }


            }

            Text(
                modifier = Modifier.padding(top = 8.dp),
//                text = "News subtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asdsubtitle ahsd dikwjmasdansi Novonoasdj SNasnjd. asd",
                text = newsArticle.content,
                fontSize = 14.sp,
//                lineHeight = 1.dp,
                maxLines = 4,
                lineHeight = 18.sp,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .alpha(0.4f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                val imgUrl = "https://icons.duckduckgo.com/ip2/${newsArticle.source}.ico"
                AsyncImage(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(21.dp)
                        .height(21.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentDescription = null,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgUrl)
                        .diskCacheKey(imgUrl) // Use URL as cache key
//                        .networkCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build()

                )
                Text(
//                    text = "url.com",
                    text = newsArticle.source,
                    color = ColorMaster.tertiary,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.7f)
                )
                Text(
                    modifier = Modifier.alpha(0.6f),
//                    text = "2024-12-21"
                    text = newsArticle.date
                )
            }
        }
    }
}
