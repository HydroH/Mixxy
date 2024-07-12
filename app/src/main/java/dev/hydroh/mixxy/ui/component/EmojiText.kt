package dev.hydroh.mixxy.ui.component

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import dev.hydroh.mixxy.util.EmojiAnnotatedString
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun EmojiText(
    text: String,
    emojis: ImmutableMap<String, EmojiData>,
    modifier: Modifier = Modifier,
    externalEmojiMap: Map<String, String>? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null
) {
    if (externalEmojiMap.isNullOrEmpty()) {
        LocalEmojiText(
            text = text,
            emojis = emojis,
            modifier = modifier,
            fontSize = fontSize,
            fontWeight = fontWeight,
        )
    } else {
        ExternalEmojiText(
            text = text,
            externalEmojiMap = externalEmojiMap,
            modifier = modifier,
            fontSize = fontSize,
            fontWeight = fontWeight,
        )
    }
}


@Composable
fun LocalEmojiText(
    text: String,
    emojis: ImmutableMap<String, EmojiData>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
) {
    val emojiAnnotatedString = remember {
        EmojiAnnotatedString(text)
    }
    val inlineContentMap = remember {
        mutableStateMapOf<String, InlineTextContent>()
    }
    val aspectRatioMap = remember {
        mutableStateMapOf<String, Float>()
    }

    LaunchedEffect(emojis) {
        emojiAnnotatedString.emojis.forEach { emoji ->
            if (emojis.contains(emoji)) {
                inlineContentMap[emoji] = InlineTextContent(
                    Placeholder(
                        width = 1.2.em * (aspectRatioMap[emoji] ?: 1f),
                        height = 1.2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    AsyncImage(
                        model = emojis[emoji]!!.url,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        onSuccess = {
                            it.result.drawable.toBitmap().apply {
                                aspectRatioMap[emoji] = width.toFloat() / height.toFloat()
                            }
                        }
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

@Composable
fun ExternalEmojiText(
    text: String,
    externalEmojiMap: Map<String, String>,
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
    val aspectRatioMap = remember {
        mutableStateMapOf<String, Float>()
    }

    LaunchedEffect(aspectRatioMap.toMap()) {
        emojiAnnotatedString.emojis.forEach { emoji ->
            if (externalEmojiMap.contains(emoji)) {
                inlineContentMap[emoji] = InlineTextContent(
                    Placeholder(
                        width = 1.2.em * (aspectRatioMap[emoji] ?: 1f),
                        height = 1.2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    AsyncImage(
                        model = externalEmojiMap[emoji],
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        onSuccess = {
                            it.result.drawable.toBitmap().apply {
                                aspectRatioMap[emoji] = width.toFloat() / height.toFloat()
                            }
                        }
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
