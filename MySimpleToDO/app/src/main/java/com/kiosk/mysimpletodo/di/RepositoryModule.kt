package com.kiosk.mysimpletodo.di

import com.kiosk.mysimpletodo.database.TodoDao
import com.kiosk.mysimpletodo.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideTodoRepository(
        todoDao: TodoDao
    ): TodoRepository = TodoRepository(todoDao)

}