package dev.hydroh.mfm.plugin.emoji

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.inlineparser.InlineProcessor
import io.noties.markwon.inlineparser.MarkwonInlineParser
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import java.util.regex.Pattern

class EmojiPlugin(private val emojiMap: Map<String, String>) : AbstractMarkwonPlugin() {
    companion object {
        fun create(emojiMap: Map<String, String>): EmojiPlugin = EmojiPlugin(emojiMap)
        private val EMOJI_REGEX = Pattern.compile("^:[a-zA-Z0-9_-]+:")
    }

    override fun configureParser(builder: Parser.Builder) {
        builder.inlineParserFactory(
            MarkwonInlineParser.factoryBuilder()
                .addInlineProcessor(
                    object: InlineProcessor() {
                        override fun specialCharacter(): Char = ':'
                        override fun parse(): Node? =
                            match(EMOJI_REGEX)?.let {
                                val name = it.substring(1, it.length - 1)
                                emojiMap[name]?.let { url ->
                                    Emoji(name, url)
                                }
                            }
                    }
                )
                .build()
        )
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(Emoji::class.java) { visitor, emoji ->
            val length = visitor.length()
            visitor.visitChildren(emoji)
            EmojiProps.NAME.set(visitor.renderProps(), emoji.name)
            EmojiProps.URL.set(visitor.renderProps(), emoji.url)
            visitor.setSpansForNodeOptional(emoji, length)
        }
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(Emoji::class.java) { configuration, props ->
            AsyncDrawableSpan(
                configuration.theme(),
                AsyncDrawable(
                    EmojiProps.URL.require(props),
                    configuration.asyncDrawableLoader(),
                    configuration.imageSizeResolver(),
                    null,
                ),
                AsyncDrawableSpan.ALIGN_BASELINE,
                false,
            )
        }
    }
}
