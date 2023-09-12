package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.hydroh.mixxy.data.local.model.EmojiData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiSelectionGrid(
    emojis: SnapshotStateMap<String, EmojiData>,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(80.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier,
    ) {
        emojis.values.asSequence()
            .sortedBy { it.name }
            .groupBy { it.category }
            .toList()
            .sortedBy { it.first }
            .map { (category, emojis) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(text = category ?: "未分组")
                }
                items(emojis) { emoji ->
                    AsyncImage(
                        model = emoji.url,
                        contentDescription = null,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(72.dp)
                            .clickable { onEmojiSelected(":${emoji.name}@.:") }
                            .padding(4.dp)
                    )
                }
            }
    }
}
