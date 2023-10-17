package com.curso.todoapp.addtasks.data.di

import android.content.Context
import androidx.room.Room
import com.curso.todoapp.addtasks.data.TaskDao
import com.curso.todoapp.addtasks.data.TodoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDao(todoDataBase: TodoDataBase): TaskDao {
        return todoDataBase.taskDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDataBase {
        return Room.databaseBuilder(context, TodoDataBase::class.java, "TaskDatabase").build()
    }
}
