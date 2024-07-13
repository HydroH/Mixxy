package dev.hydroh.mfm.plugin.emoji

import io.noties.markwon.Prop
import org.commonmark.node.CustomNode

class Emoji(val name: String, val url: String) : CustomNode()

object EmojiProps {
    val NAME = Prop.of<String>("name")
    val URL = Prop.of<String>("url")
}