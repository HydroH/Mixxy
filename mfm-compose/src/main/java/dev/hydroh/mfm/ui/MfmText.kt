package dev.hydroh.mfm.ui

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.github.samunohito.mfm.api.Mfm
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmBold
import com.github.samunohito.mfm.api.node.MfmEmojiCode
import com.github.samunohito.mfm.api.node.MfmHashtag
import com.github.samunohito.mfm.api.node.MfmLink
import com.github.samunohito.mfm.api.node.MfmMention
import com.github.samunohito.mfm.api.node.MfmNodeType
import com.github.samunohito.mfm.api.node.MfmQuote
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.node.MfmUrl

@Composable
fun MfmText(
    markdown: String,
    emojiUrlMap: Map<String, String>,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val colorScheme = MaterialTheme.colorScheme
    val annotatedString = remember {
        val mfmTree = Mfm.parseSimple(markdown)
        buildAnnotatedString {
            mfmTree.forEach { it.appendAnnotatedString(colorScheme) }
        }
    }
    val inlineContentMap = remember {
        mutableStateMapOf<String, InlineTextContent>()
    }
    val emojiDimsMap = remember {
        mutableStateMapOf<String, Pair<Int, Int>>()
    }
    LaunchedEffect(emojiDimsMap.toMap()) {
        annotatedString.getStringAnnotations(MfmNodeType.EmojiCode.name, 0, annotatedString.length)
            .forEach {
                val emoji = it.item
                emojiUrlMap[emoji]?.let { url ->
                    inlineContentMap[emoji] = InlineTextContent(
                        placeholder = Placeholder(
                            width = style.lineHeight * (emojiDimsMap[emoji]?.let {
                                it.first.toFloat() / it.second.toFloat()
                            } ?: 1f),
                            height = style.lineHeight,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
                        )
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = emoji,
                            contentScale = ContentScale.FillHeight,
                            onSuccess = {
                                it.result.drawable.toBitmap().apply {
                                    emojiDimsMap[emoji] = Pair(width, height)
                                }
                            }
                        )
                    }
                }
            }
    }
    SelectionContainer {
        Text(
            text = annotatedString,
            modifier = modifier,
            style = style,
            inlineContent = inlineContentMap
        )
    }
}

context (AnnotatedString.Builder)
fun IMfmNode.appendAnnotatedString(colorScheme: ColorScheme) {
    when (this) {
        is MfmText -> append(props.text)
        is MfmMention -> {
            withLink(
                LinkAnnotation.Url("mfm://accounts/${props.acct}")
            ) {
                append(props.acct)
            }
        }

        is MfmHashtag -> {
            withLink(
                LinkAnnotation.Url("mfm://topics/${props.hashtag}")
            ) {
                append(stringify())
            }
        }

        is MfmUrl -> {
            withLink(
                LinkAnnotation.Url(props.url)
            ) {
                append(stringify())
            }
        }

        is MfmLink -> {
            withLink(
                LinkAnnotation.Url(props.url)
            ) {
                children.forEach { it.appendAnnotatedString(colorScheme) }
            }
        }

        is MfmEmojiCode -> {
            pushStringAnnotation(
                tag = MfmNodeType.EmojiCode.name,
                annotation = props.name
            )
            appendInlineContent(props.name, alternateText = stringify())
            pop()
        }

        is MfmBold -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            children.forEach { it.appendAnnotatedString(colorScheme) }
        }

        is MfmQuote -> withStyle(ParagraphStyle(textIndent = TextIndent(8.sp, 8.sp))) {
            withStyle(SpanStyle(color = colorScheme.onSurface.copy(alpha = 0.6f))) {
                children.forEach { it.appendAnnotatedString(colorScheme) }
            }
        }
    }
}

@Preview
@Composable
fun MfmTextPreview() {
    MfmText(
        markdown = "Hello, **world**! :smile:",
        emojiUrlMap = mapOf(
            "smile" to "https://example.com/smile.png"
        )
    )
}