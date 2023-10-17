package com.curso.todoapp.addtasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curso.todoapp.addtasks.domain.AddTaskUseCase
import com.curso.todoapp.addtasks.domain.DeleteTaskUseCase
import com.curso.todoapp.addtasks.domain.GetTasksUseCase
import com.curso.todoapp.addtasks.domain.UpdateTaskUseCase
import com.curso.todoapp.addtasks.ui.TaskUIState.Error
import com.curso.todoapp.addtasks.ui.TaskUIState.Loading
import com.curso.todoapp.addtasks.ui.TaskUIState.Success
import com.curso.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUIState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private var _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

//    private val _tasks = mutableStateListOf<TaskModel>()
//    val tasks: List<TaskModel> = _tasks

    fun dialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String) {
        _showDialog.value = false
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        // Actualizar check
//        val index = _tasks.indexOf(taskModel)
//        _tasks[index] = _tasks[index].let { task ->
//            task.copy(selected = !task.selected)
//        }
        viewModelScope.launch {
            updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        // Borrar task
//        val task = _tasks.find { tasks -> tasks.id == taskModel.id }
//        _tasks.remove(task)
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}
