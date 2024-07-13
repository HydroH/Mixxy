package dev.hydroh.mfm.ui

import android.content.Context
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import dev.hydroh.mfm.plugin.emoji.EmojiPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.coil.CoilImagesPlugin

@Composable
fun MfmText(
    markdown: String,
    emojiMap: Map<String, String>,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val context = LocalContext.current
    val markwon = remember { newMfmMarkwon(context, emojiMap) }
}

private fun newMfmMarkwon(context: Context, emojiMap: Map<String, String>): Markwon =
    Markwon.builder(context)
        .usePlugin(CorePlugin.create())
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(CoilImagesPlugin.create(context))
        .usePlugin(EmojiPlugin.create(emojiMap))
        .build()