package com.curso.todoapp.addtasks.data

import com.curso.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<TaskModel>> = taskDao.getTasks().map { value: List<TaskEntity> ->
        value.map { taskEntity ->
            TaskModel(
                taskEntity.id,
                taskEntity.task,
                taskEntity.selected
            )
        }
    }

    suspend fun add(taskModel: TaskModel) {
        taskDao.addTask(taskModel.toData())
    }

    suspend fun update(taskModel: TaskModel) {
        taskDao.updateTask(taskModel.toData())
    }

    suspend fun delete(taskModel: TaskModel) {
        taskDao.deleteTask(taskModel.toData())
    }
}

fun TaskModel.toData(): TaskEntity{
    return TaskEntity(this.id, this.task, this.selected)
}