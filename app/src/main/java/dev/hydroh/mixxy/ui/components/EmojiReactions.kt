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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiReactions(
    reactions: Map<String, Int>,
    myReaction: String?,
    onCreateReaction: (String) -> Unit,
    onDeleteReaction: () -> Unit,
    emojis: ImmutableMap<String, EmojiData>,
    modifier: Modifier = Modifier,
    externalEmojiMap: Map<String, String>? = null,
) {
    var isLoading = rememberSaveable { false }
    LaunchedEffect(reactions) {
        isLoading = false
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        reactions.toList().sortedByDescending { (_, v) -> v }.map { (emoji, count) ->
            key(emoji, count) {
                Button(
                    onClick = {
                        if (isLoading) return@Button
                        if (emoji == myReaction) {
                            isLoading = true
                            onDeleteReaction()
                        } else {
                            isLoading = true
                            onCreateReaction(emoji)
                        }
                    },
                    enabled = emoji.contains("@.") || !emoji.contains(":"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (emoji == myReaction) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (emoji == myReaction) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.requiredHeight(32.dp)
                ) {
                    if (emoji.contains("@.")) {
                        LocalEmojiText(
                            text = "${emoji.replace("@.", "")} $count",
                            emojis = emojis,
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
}
