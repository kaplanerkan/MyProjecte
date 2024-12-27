package com.kiosk.mysimpletodo.repository

import androidx.lifecycle.LiveData
import com.kiosk.mysimpletodo.database.TodoDao
import com.kiosk.mysimpletodo.model.Todo
import com.kiosk.mysimpletodo.ui.SortOrder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {

    fun getAllTodos(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Todo>> {
        return when(sortOrder) {
            SortOrder.BY_TITLE        -> {
                todoDao.getAllTodoSortedByTitle(searchQuery, hideCompleted).flowOn(Dispatchers.IO)
            }
            SortOrder.BY_DATE_CREATED -> {
                todoDao.getAllTodoSortedByDateCreated(searchQuery, hideCompleted).flowOn(Dispatchers.IO)
            }
        }
    }

    fun getAllTodosBySearch(searchQuery: String): Flow<List<Todo>> {
        return todoDao.getAllTodoBySearch(searchQuery).flowOn(Dispatchers.IO)
    }

    suspend fun saveNewTodo(todo: Todo): Long  {
         return withContext(Dispatchers.IO) {
             todoDao.insert(todo)
         }
    }

    suspend fun deleteTodo(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoDao.delete(todo)
        }
    }

    suspend fun updateTodoStatus(id: Int, isDone: Boolean) {
        withContext(Dispatchers.IO) {
            todoDao.setTodoIsDone(id, isDone)
        }
    }
}