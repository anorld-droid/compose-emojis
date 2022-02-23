package com.example.ios_emoji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ios_emoji.ui.theme.ComposeEmojiTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeEmojiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EmojiTable(
                        onTextAdded = {},
                        scope = rememberCoroutineScope(),
                        headerBackgroundColor = backgroundColor,
                        headerContentColor = Color.Black,
                        headerIconTint = Color.Blue,
                        bodyBackgroundColor = Color.DarkGray
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EmojiTable(
    onTextAdded: (String) -> Unit,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    headerBackgroundColor: Color,
    headerContentColor: Color,
    bodyBackgroundColor: Color,
    headerIconTint: Color
) {
    val tabs = IosEmojiProvider().getCategories().toList()
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Column(modifier.fillMaxWidth()) {
        Tabs(
            tabs = tabs,
            pagerState = pagerState,
            scope = scope,
            backgroundColor = headerBackgroundColor,
            contentColor = headerContentColor,
            headerIconTint = headerIconTint
        )
        TabsContent(
            tabs = tabs,
            pagerState = pagerState,
            onTextAdded = onTextAdded,
            backgroundColor = bodyBackgroundColor
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun Tabs(
    tabs: List<IosEmojiCategory>,
    pagerState: PagerState,
    scope: CoroutineScope,
    backgroundColor: Color,
    contentColor: Color,
    headerIconTint: Color
) {
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        indicator = { tabPosition ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPosition)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                icon = {
                    Icon(
                        painterResource(id = tab.icon),
                        tint = headerIconTint,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}


@ExperimentalPagerApi
@Composable
private fun TabsContent(
    tabs: List<IosEmojiCategory>,
    pagerState: PagerState,
    onTextAdded: (String) -> Unit,
    backgroundColor: Color,
) {
    HorizontalPager(state = pagerState) { page ->
        val rows: Int =
            if (tabs[page].emojis.size % EMOJI_COLUMNS == 0) tabs[page].emojis.size / EMOJI_COLUMNS else (tabs[page].emojis.size / EMOJI_COLUMNS) + 1
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                repeat(rows) { x ->
                    val emojis = tabs[page].emojis.toList()
                        .subList((x - 1) * EMOJI_COLUMNS, x * EMOJI_COLUMNS)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = backgroundColor),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(EMOJI_COLUMNS) { y ->
                            val emoji =
                                if (emojis.size <= y) emojis[(y - 1)] else emojis[emojis.lastIndex]
                            Text(
                                modifier = Modifier
                                    .clickable(onClick = { onTextAdded(emoji.unicode) })
                                    .sizeIn(minWidth = 62.dp, minHeight = 62.dp)
                                    .padding(8.dp),
                                text = emoji.unicode,
                                style = LocalTextStyle.current.copy(
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val EMOJI_COLUMNS = 10