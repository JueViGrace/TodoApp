@file:OptIn(ExperimentalMaterial3Api::class)

package com.curso.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.curso.todoapp.R
import com.curso.todoapp.addtasks.ui.model.TaskModel

@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val showDialog: Boolean by tasksViewModel.showDialog.observeAsState(initial = false)

    val uiState by produceState<TaskUIState>(
        initialValue = TaskUIState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TaskUIState.Error -> {
        }
        TaskUIState.Loading -> {
            CircularProgressIndicator()
        }
        is TaskUIState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                AddTasksDialog(
                    showDialog,
                    onDismiss = { tasksViewModel.dialogClose() },
                    onTaskAdded = { tasksViewModel.onTaskCreated(it) }
                )
                FabDialog(modifier = Modifier.align(Alignment.BottomEnd), tasksViewModel)
                TasksList((uiState as TaskUIState.Success).task, tasksViewModel)
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, tasksViewModel: TasksViewModel) {
//    val myTasks: List<TaskModel> = tasksViewModel.tasks

    LazyColumn {
        items(tasks, key = { item -> item.id }) { task ->
            ItemTask(taskModel = task, tasksViewModel = tasksViewModel)
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        tasksViewModel.onItemRemove(taskModel)
                    }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = taskModel.task,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
            Checkbox(checked = taskModel.selected, onCheckedChange = { tasksViewModel.onCheckBoxSelected(taskModel) })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(onClick = { tasksViewModel.onShowDialogClick() }, modifier = modifier.padding(16.dp)) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}

@Composable
fun AddTasksDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by rememberSaveable {
        mutableStateOf("")
    }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_task),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = { myTask = it }, singleLine = true, maxLines = 1)
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.add_task))
                }
            }
        }
    }
}
