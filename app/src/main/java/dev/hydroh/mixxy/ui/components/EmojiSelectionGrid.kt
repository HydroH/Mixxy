package dev.hydroh.mixxy.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.hydroh.mixxy.R
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun EmojiSelectionGrid(
    emojis: ImmutableMap<String, EmojiData>,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandedColumn by remember { mutableStateOf(null as String?) }
    LazyColumn(modifier = modifier) {
        emojis.values.asSequence()
            .sortedBy { it.name }
            .groupBy { it.category }
            .toList()
            .sortedBy { it.first }
            .map { (category, emojis) ->
                item {
                    val rotationState by animateFloatAsState(if (expandedColumn == category) 180f else 0f)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedColumn = if (expandedColumn == category) null else category
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(text = category ?: "未分组")
                        Icon(
                            painter = painterResource(id = R.drawable.expand_more_20px),
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationState)
                        )
                    }
                    AnimatedVisibility(
                        visible = expandedColumn == category,
                        enter = expandVertically(),
                        exit = shrinkVertically(),
                    ) {
                        VerticalGrid(
                            columns = 5,
                            itemCount = emojis.count(),
                            spacing = 8.dp,
                        ) { index ->
                            AsyncImage(
                                model = emojis[index].url,
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onEmojiSelected(":${emojis[index].name}@.:") }
                            )
                        }
                    }
                }
            }
    }
}
