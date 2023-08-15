package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import dev.hydroh.mixxy.data.local.model.EmojiData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiReactions(
    reactions: Map<String, Int>,
    onClick: (String) -> Unit,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    externalEmojiMap: Map<String, String>? = null,
) {
    FlowRow(
        modifier = modifier,
    ) {
        reactions.forEach { (emoji, count) ->
            Button(onClick = { onClick(emoji) }) {
                if (emoji.contains("@.")) {
                    LocalEmojiText(
                        text = "${emoji.replace("@.", "")} $count",
                        emojiMap = emojiMap,
                        updateEmojis = updateEmojis,
                    )
                } else {
                    ExternalEmojiText(
                        text = "$emoji $count",
                        externalEmojiMap = externalEmojiMap ?: mapOf(),
                    )
                }
            }
        }
    }
}