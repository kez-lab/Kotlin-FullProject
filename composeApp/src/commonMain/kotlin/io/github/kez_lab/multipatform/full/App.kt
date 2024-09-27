@file:OptIn(ExperimentalMaterialApi::class)

package io.github.kez_lab.multipatform.full

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kez_lab.multipatform.full.model.Priority
import io.github.kez_lab.multipatform.full.model.Task
import io.github.kez_lab.multipatform.full.network.TaskApi
import io.github.kez_lab.multipatform.full.network.getHttpClient
import io.github.kez_lab.multipatform.full.ui.dialog.AddTaskDialog
import io.github.kez_lab.multipatform.full.ui.dialog.UpdateTaskDialog
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        val client = remember { TaskApi(getHttpClient("kezlab.site")) }
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        val scope = rememberCoroutineScope()
        var currentTask by remember { mutableStateOf<Task?>(null) }
        var isAddTaskDialogVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            tasks = client.getAllTasks()
        }

        if (currentTask != null) {
            UpdateTaskDialog(
                task = currentTask!!,
                onConfirm = {
                    scope.launch {
                        client.updateTask(it)
                        tasks = client.getAllTasks()
                    }
                    currentTask = null
                }
            )
        }

        if (isAddTaskDialogVisible) {
            AddTaskDialog(
                onConfirm = {
                    scope.launch {
                        client.updateTask(it)
                        tasks = client.getAllTasks()
                    }
                    isAddTaskDialogVisible = false
                },
                onDismiss = { isAddTaskDialogVisible = false }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                items(tasks) { task ->
                    TaskCard(
                        task,
                        onDelete = {
                            scope.launch {
                                client.removeTask(it)
                                tasks = client.getAllTasks()
                            }
                        },
                        onUpdate = {
                            currentTask = task
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { isAddTaskDialogVisible = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onDelete: (Task) -> Unit,
    onUpdate: (Task) -> Unit
) {
    fun pickWeight(priority: Priority) = when (priority) {
        Priority.Low -> FontWeight.SemiBold
        Priority.Medium -> FontWeight.Bold
        Priority.High, Priority.Vital -> FontWeight.ExtraBold
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(CornerSize(4.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                "${task.name}: ${task.description}",
                fontSize = 20.sp,
                fontWeight = pickWeight(task.priority)
            )

            Row {
                OutlinedButton(onClick = { onDelete(task) }) {
                    Text("Delete")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = { onUpdate(task) }) {
                    Text("Update")
                }
            }
        }
    }
}