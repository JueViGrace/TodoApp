package com.curso.todoapp.addtasks.domain

import com.curso.todoapp.addtasks.data.TaskRepository
import com.curso.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskModel: TaskModel) = repository.delete(taskModel = taskModel)
}
