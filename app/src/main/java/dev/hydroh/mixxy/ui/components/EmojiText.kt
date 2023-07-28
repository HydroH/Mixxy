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
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
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
    val aspectRatioMap = remember {
        mutableStateMapOf<String, Float>()
    }

    LaunchedEffect(Unit) {
        updateEmojis(emojiAnnotatedString.emojis)
    }
    LaunchedEffect(emojiMap.toMap(), aspectRatioMap.toMap()) {
        emojiAnnotatedString.emojis.forEach { emoji ->
            if (emojiMap.contains(emoji)) {
                inlineContentMap[emoji] = InlineTextContent(
                    Placeholder(
                        width = 1.2.em * (aspectRatioMap[emoji] ?: 1f),
                        height = 1.2.em,
                        placeholderVerticalAlign =  PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    AsyncImage(
                        model = emojiMap[emoji]!!.url,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
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
fun EmojiText(
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
        externalEmojiMap.mapValues {  entry ->
            inlineContentMap[entry.key] = InlineTextContent(
                Placeholder(
                    width = 1.2.em * (aspectRatioMap[entry.key] ?: 1f),
                    height = 1.2.em,
                    placeholderVerticalAlign =  PlaceholderVerticalAlign.TextCenter
                )
            ) {
                AsyncImage(
                    model = entry.value,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    onSuccess = {
                        it.result.drawable.toBitmap().apply {
                            aspectRatioMap[entry.key] = width.toFloat() / height.toFloat()
                        }
                    }
                )
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
