package dev.hydroh.mixxy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteEditor(
    text: String,
    onClickSubmit: () -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Button(onClick = { onClickSubmit() }) {
                Text(text = "Submit")
            }
            OutlinedTextField(
                value = text,
                onValueChange = { onTextChange(it) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
