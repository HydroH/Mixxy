package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
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
import androidx.paging.compose.items
import coil.compose.AsyncImage
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.mixxy.data.local.model.EmojiData

@Composable
fun NoteItem(
    note: Note,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = note.user.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                EmojiText(
                    text = note.user.name ?: note.user.username,
                    emojiMap = emojiMap,
                    updateEmojis = updateEmojis,
                    externalEmojiMap = note.user.emojis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (note.user.host != null) "${note.user.username}@${note.user.host}"
                    else note.user.username,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                if (!note.text.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    EmojiText(
                        text = note.text ?: "",
                        emojiMap = emojiMap,
                        updateEmojis = updateEmojis,
                        externalEmojiMap = note.emojis,
                        fontSize = 16.sp
                    )
                }
                if (note.files.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ImageGrid(
                        files = note.files,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItemList(
    notes: LazyPagingItems<Note>,
    emojiMap: SnapshotStateMap<String, EmojiData>,
    updateEmojis: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = notes,
            key = { note -> note.id }
        ) {
            it?.let {
                NoteItem(
                    note = it,
                    emojiMap = emojiMap,
                    updateEmojis = updateEmojis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
        }
    }
}
