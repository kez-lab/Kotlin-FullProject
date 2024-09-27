package io.github.kez_lab.multipatform.full.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.kez_lab.multipatform.full.model.Task
import io.github.kez_lab.multipatform.full.ui.button.TaskDialogButtons
import io.github.kez_lab.multipatform.full.ui.dropdown.PriorityDropdown

@Composable
fun UpdateTaskDialog(
    task: Task,
    onConfirm: (Task) -> Unit
) {
    var description by remember { mutableStateOf(task.description) }
    var priority by remember { mutableStateOf(task.priority) }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            shape = RoundedCornerShape(CornerSize(4.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Update Task", fontSize = 20.sp)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue
                    )
                )

                PriorityDropdown(
                    selectedPriority = priority,
                    onPrioritySelected = { priority = it }
                )

                TaskDialogButtons(
                    onConfirm = {
                        val updatedTask = Task(
                            name = task.name,
                            description = description,
                            priority = priority
                        )
                        onConfirm(updatedTask)
                    },
                    onDismiss = {}
                )
            }
        }
    }
}
