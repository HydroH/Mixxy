package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.hydroh.mixxy.R
import dev.hydroh.mixxy.data.local.model.EmojiData
import dev.hydroh.mixxy.data.remote.model.Note
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun NoteItem(
    note: Note,
    onCreateReaction: (Note, String) -> Unit,
    onDeleteReaction: (Note) -> Unit,
    onClickReplyButton: (Note) -> Unit,
    onClickRenoteButton: (Note) -> Unit,
    onClickReactionButton: (Note) -> Unit,
    emojis: ImmutableMap<String, EmojiData>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        if (note.text.isNullOrEmpty() && note.renote != null) {
            // Renote
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                EmojiText(
                    text = "由 ${note.user.name ?: note.user.username} 转发",
                    emojis = emojis,
                    externalEmojiMap = note.user.emojis,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                NoteItem(
                    note = note.renote!!,
                    onCreateReaction = { _, reaction ->
                        onCreateReaction(note.renote!!, reaction)
                    },
                    onDeleteReaction = { onDeleteReaction(note.renote!!) },
                    onClickReplyButton = { onClickReplyButton(note.renote!!) },
                    onClickRenoteButton = { onClickRenoteButton(note.renote!!) },
                    onClickReactionButton = { onClickReactionButton(note.renote!!) },
                    emojis = emojis,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Avatar
                AsyncImage(
                    model = note.user.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    // Username
                    EmojiText(
                        text = note.user.name ?: note.user.username,
                        emojis = emojis,
                        externalEmojiMap = note.user.emojis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    // UserHandle
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (note.user.host != null) {
                            "@${note.user.username}@${note.user.host}"
                        } else {
                            "@${note.user.username}"
                        },
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    // Text
                    if (!note.text.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        EmojiText(
                            text = note.text ?: "",
                            emojis = emojis,
                            externalEmojiMap = note.emojis,
                            fontSize = 16.sp
                        )
                    }

                    // Images
                    if (note.files.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        ImageGrid(
                            files = note.files,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        )
                    }

                    // Reactions
                    if (note.reactions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        EmojiReactions(
                            reactions = note.reactions,
                            myReaction = note.myReaction,
                            onCreateReaction = { reaction ->
                                onCreateReaction(note, reaction)
                            },
                            onDeleteReaction = {
                                onDeleteReaction(note)
                            },
                            emojis = emojis,
                            modifier = Modifier
                                .fillMaxWidth(),
                            externalEmojiMap = note.reactionEmojis,
                        )
                    }

                    // Buttons
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { onClickReplyButton(note) }) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.reply_20px),
                                    contentDescription = null,
                                )
                                if (note.repliesCount > 0) {
                                    Text(" ${note.repliesCount}")
                                }
                            }
                        }
                        IconButton(onClick = { onClickRenoteButton(note) }) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.repeat_20px),
                                    contentDescription = null,
                                )
                                if (note.renoteCount > 0) {
                                    Text(" ${note.renoteCount}")
                                }
                            }
                        }
                        IconButton(onClick = { onClickReactionButton(note) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_20px),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}
