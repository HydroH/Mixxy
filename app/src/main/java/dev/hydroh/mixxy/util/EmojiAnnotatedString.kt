package dev.hydroh.mixxy.util

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString

class EmojiAnnotatedString(text: String) {
    val annotatedString: AnnotatedString
    val emojis = arrayListOf<String>()

    init {
        annotatedString = buildAnnotatedString {
            var startIndex = 0
            Regex(""":(.*?):""", RegexOption.MULTILINE)
                .findAll(text).forEach {
                    append(text.substring(startIndex, it.range.first))
                    appendInlineContent(it.groupValues[1], it.value)
                    emojis.add(it.groupValues[1])
                    startIndex = it.range.last + 1
                }
            if (startIndex < text.length)
                append(text.substring(startIndex, text.length))
        }
    }
}