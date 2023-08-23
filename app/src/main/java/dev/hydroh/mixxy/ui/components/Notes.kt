package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.mixxy.data.local.model.EmojiData

@Composable
fun NoteItem(
    note: Note,
    onCreateReaction: (Note, String) -> Unit,
    onDeleteReaction: (Note) -> Unit,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        if (note.text.isNullOrEmpty() && note.renote != null) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                EmojiText(
                    text = "由 ${note.user.name ?: note.user.username} 转发",
                    emojiMap = emojiMap,
                    updateEmojis = updateEmojis,
                    externalEmojiMap = note.user.emojis,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp, 12.dp, 0.dp, 0.dp)
                )
                NoteItem(
                    note = note.renote!!,
                    onCreateReaction = { _, reaction ->
                        onCreateReaction(note.renote!!, reaction)
                    },
                    onDeleteReaction = {
                        onDeleteReaction(note.renote!!)
                    },
                    emojiMap = emojiMap,
                    updateEmojis = updateEmojis,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
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
                        emojiMap = emojiMap,
                        updateEmojis = updateEmojis,
                        externalEmojiMap = note.user.emojis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    // UserHandle
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (note.user.host != null) "@${note.user.username}@${note.user.host}"
                        else "@${note.user.username}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    // Text
                    if (!note.text.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        EmojiText(
                            text = note.text ?: "",
                            emojiMap = emojiMap,
                            updateEmojis = updateEmojis,
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
                            emojiMap = emojiMap,
                            updateEmojis = updateEmojis,
                            modifier = Modifier
                                .fillMaxWidth(),
                            externalEmojiMap = note.reactionEmojis,
                        )
                    }

                    // Buttons
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        // TODO
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItemList(
    notes: LazyPagingItems<Note>,
    onCreateReaction: (Note, String) -> Unit,
    onDeleteReaction: (Note) -> Unit,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = notes.itemCount,
            key = { index -> notes[index]?.id ?: "" },
        ) { index ->
            notes[index]?.let {
                NoteItem(
                    note = it,
                    onCreateReaction = onCreateReaction,
                    onDeleteReaction = onDeleteReaction,
                    emojiMap = emojiMap,
                    updateEmojis = updateEmojis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
