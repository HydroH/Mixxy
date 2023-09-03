package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
    LazyColumn(modifier = modifier) {
        emojis.values.asSequence()
            .sortedBy { it.name }
            .groupBy { it.category }
            .toList()
            .sortedBy { it.first }
            .map { (category, emojis) ->
                item {
                    Text(text = category ?: "未分组") // TODO: Expandable
                    FlowRow {
                        emojis.forEach { emoji ->
                            AsyncImage(
                                model = emoji.url,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .clickable { onEmojiSelected(":${emoji.name}@.:") }
                                    .height(40.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }.toList()
    }
}