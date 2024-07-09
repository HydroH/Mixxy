package dev.hydroh.mixxy.ui.components

import android.view.Gravity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import dev.hydroh.mixxy.data.remote.model.Note
import kotlinx.collections.immutable.ImmutableMap

sealed class NoteAction {
    data class Create(val text: String, val replyID: String? = null, val renoteID: String? = null) :
        NoteAction()

    class Renote(val note: Note) : NoteAction()
    class Reaction(val note: Note) : NoteAction()
}

class NoteActionDialogState {
    var noteAction: NoteAction? by mutableStateOf(null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionDialog(
    noteActionDialogState: NoteActionDialogState,
    sheetState: SheetState,
    emojis: ImmutableMap<String, EmojiData>,
    onCreateNote: (text: String, replyID: String?, renoteID: String?) -> Unit,
    onRenote: (noteID: String) -> Unit,
    onReaction: (note: Note, reaction: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    noteActionDialogState.noteAction?.let {
        when (it) {
            is NoteAction.Create -> {
                BoxWithConstraints {
                    Dialog(
                        onDismissRequest = {
                            noteActionDialogState.noteAction = null
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false,
                        ),
                    ) {
                        (LocalView.current.parent as DialogWindowProvider).window.setGravity(Gravity.TOP)
                        NoteEditor(
                            text = it.text,
                            onClickSubmit = { onCreateNote(it.text, it.replyID, it.renoteID) },
                            onTextChange = { text ->
                                noteActionDialogState.noteAction = it.copy(text = text)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(Dp.Hairline, maxHeight * 0.5f)
                                .padding(20.dp)
                        )
                    }
                }
            }

            is NoteAction.Renote -> {
                ModalBottomSheet(
                    onDismissRequest = {
                        noteActionDialogState.noteAction = null
                    },
                    sheetState = sheetState
                ) {
                    Column {
                        Text(
                            text = "转发",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRenote(it.note.id) }
                                .padding(8.dp, 6.dp)
                        )
                        Text(
                            text = "引用",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    noteActionDialogState.noteAction = NoteAction.Create(
                                        "",
                                        replyID = it.note.id
                                    )
                                }
                                .padding(8.dp, 6.dp)
                        )
                    }
                }
            }

            is NoteAction.Reaction -> {
                ModalBottomSheet(
                    onDismissRequest = {
                        noteActionDialogState.noteAction = null
                    },
                    sheetState = sheetState
                ) {
                    EmojiSelectionGrid(
                        emojis = emojis,
                        onEmojiSelected = { emoji ->
                            onReaction(it.note, emoji)
                        }
                    )
                }
            }
        }
    }
}
