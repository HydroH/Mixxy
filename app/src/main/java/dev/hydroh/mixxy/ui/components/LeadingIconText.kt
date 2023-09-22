package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em

@Composable
fun LeadingIconText(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null
) {
    Text(
        text = buildAnnotatedString {
            appendInlineContent("icon", contentDescription ?: "icon")
            append(text)
        },
        modifier = modifier,
        fontWeight = fontWeight,
        fontSize = fontSize,
        inlineContent = mapOf(
            "icon" to InlineTextContent(
                Placeholder(
                    width = 1.em,
                    height = 1.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Icon(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize()
                )
            }
        ),
    )
}
