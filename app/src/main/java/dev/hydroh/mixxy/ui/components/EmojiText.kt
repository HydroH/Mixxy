package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.hydroh.mixxy.data.local.model.EmojiData
import dev.hydroh.mixxy.util.EmojiAnnotatedString

@Composable
fun EmojiText(
    text: String,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null
) {
    val emojiAnnotatedString = remember {
        EmojiAnnotatedString(text)
    }
    val inlineContentMap = remember {
        mutableStateMapOf<String, InlineTextContent>()
    }

    LaunchedEffect(Unit) {
        updateEmojis(emojiAnnotatedString.emojis)
    }
    LaunchedEffect(emojiMap.size) {
        emojiAnnotatedString.emojis.forEach { name ->
            if (emojiMap.contains(name)) {
                inlineContentMap[name] = InlineTextContent(
                    Placeholder(
                        1.2.em,
                        1.2.em,
                        PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    CoilImage(
                        imageModel = { emojiMap[name]!!.url },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Fit
                        )
                    )
                }
            }
        }
    }

    Text(
        text = emojiAnnotatedString.annotatedString,
        inlineContent = inlineContentMap,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = fontWeight,
    )
}