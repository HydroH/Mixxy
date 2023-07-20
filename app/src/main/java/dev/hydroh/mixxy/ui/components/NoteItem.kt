package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoteItem(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "NoteItem")
    }
}

@Preview(name = "NoteItem")
@Composable
private fun NoteItemPreview() {
    NoteItem()
}