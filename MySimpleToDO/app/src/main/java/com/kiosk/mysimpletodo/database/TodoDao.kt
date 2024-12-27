package com.kiosk.mysimpletodo.database

import androidx.room.*
import com.kiosk.mysimpletodo.model.Todo
import com.kiosk.mysimpletodo.ui.SortOrder

import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Todo): Long

    fun getAllTodos(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean) =
        when(sortOrder) {
            SortOrder.BY_TITLE -> getAllTodoSortedByTitle(searchQuery, hideCompleted)
            SortOrder.BY_DATE_CREATED -> getAllTodoSortedByDateCreated(searchQuery, hideCompleted)
        }

    @Query("SELECT * FROM todo WHERE (is_task_done != :hideCompleted OR is_task_done = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY title")
    fun getAllTodoSortedByTitle(searchQuery: String, hideCompleted: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE (is_task_done != :hideCompleted OR is_task_done = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY created_at DESC")
    fun getAllTodoSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE title LIKE '%' || :searchQuery || '%'")
    fun getAllTodoBySearch(searchQuery: String): Flow<List<Todo>>

    @Delete
    fun delete(data: Todo)

    @Query("UPDATE todo SET  is_task_done = :isDone WHERE id = :id")
    fun setTodoIsDone(id: Int, isDone: Boolean)
}