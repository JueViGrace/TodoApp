package com.curso.todoapp.addtasks.ui

import com.curso.todoapp.addtasks.ui.model.TaskModel

sealed interface TaskUIState {
    data object Loading : TaskUIState
    data class Error(val throwable: Throwable) : TaskUIState
    data class Success(val task: List<TaskModel>) : TaskUIState
}
