package dev.hydroh.mfm.ui

import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
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
    emojiMap: Map<String, String>,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    onClickMention: (String) -> Unit = {},
    onClickHashTag: (String) -> Unit = {},
    onClickUrl: (String) -> Unit = {},
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
    val emojiAspectRatioMap = remember {
        mutableStateMapOf<String, Float>()
    }
    LaunchedEffect(emojiAspectRatioMap.toMap()) {
        annotatedString.getStringAnnotations(MfmNodeType.EmojiCode.name, 0, annotatedString.length).forEach {
            val emoji = it.item
            emojiMap[emoji]?.let { url ->
                inlineContentMap[emoji] = InlineTextContent(
                    placeholder = Placeholder(
                        width = 1.2.em * (emojiAspectRatioMap[emoji]?:1f),
                        height = 1.2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
                    )
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = emoji,
                        contentScale = ContentScale.FillHeight,
                        onSuccess = {
                            it.result.drawable.toBitmap().apply {
                                emojiAspectRatioMap[emoji] = width.toFloat() / height.toFloat()
                            }
                        }
                    )
                }
            }
        }
    }
    SelectionContainer {
        ClickableText(text = annotatedString, modifier = modifier, style = style, onClick = {
            annotatedString.getStringAnnotations(it, it).firstOrNull()?.let { annotation ->
                when (annotation.tag) {
                    MfmNodeType.Mention.name -> onClickMention(annotation.item)
                    MfmNodeType.HashTag.name -> onClickHashTag(annotation.item)
                    MfmNodeType.Url.name -> onClickUrl(annotation.item)
                }
            }
        })
    }
}

context (AnnotatedString.Builder)
fun IMfmNode.appendAnnotatedString(colorScheme: ColorScheme) {
    when (this) {
        is MfmText -> append(props.text)
        is MfmMention -> {
            pushStringAnnotation(
                tag = MfmNodeType.Mention.name,
                annotation = props.acct
            )
            withStyle(SpanStyle(color = colorScheme.primary)) {
                append(props.acct)
            }
            pop()
        }
        is MfmHashtag -> {
            pushStringAnnotation(
                tag = MfmNodeType.HashTag.name,
                annotation = props.hashtag
            )
            withStyle(SpanStyle(color = colorScheme.primary)) {
                append(stringify())
            }
            pop()
        }
        is MfmUrl -> {
            pushStringAnnotation(
                tag = MfmNodeType.Url.name,
                annotation = props.url
            )
            withStyle(SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline)) {
                append(props.url)
            }
            pop()
        }
        is MfmLink -> {
            pushStringAnnotation(
                tag = MfmNodeType.Url.name,
                annotation = props.url
            )
            withStyle(SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline)) {
                children.forEach { it.appendAnnotatedString(colorScheme) }
            }
            pop()
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
