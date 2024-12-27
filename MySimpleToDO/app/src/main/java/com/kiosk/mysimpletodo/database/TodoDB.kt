package com.kiosk.mysimpletodo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiosk.mysimpletodo.model.Todo

@Database(
    version = 1,
    entities = [
        Todo::class
    ],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDB: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}