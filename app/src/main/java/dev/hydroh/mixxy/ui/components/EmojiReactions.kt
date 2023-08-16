package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        reactions.toList().sortedByDescending { (_, v) -> v }.forEach { (emoji, count) ->
            Button(
                onClick = { onClick(emoji) },
                enabled = emoji.contains("@.") || !emoji.contains(":"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.requiredHeight(32.dp)
            ) {
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