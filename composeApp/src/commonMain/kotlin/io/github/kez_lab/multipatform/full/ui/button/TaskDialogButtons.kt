package io.github.kez_lab.multipatform.full.ui.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskDialogButtons(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel"
) {
    Row {
        OutlinedButton(
            onClick = { onDismiss() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(dismissText)
        }
        OutlinedButton(
            onClick = { onConfirm() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(confirmText)
        }
    }
}
