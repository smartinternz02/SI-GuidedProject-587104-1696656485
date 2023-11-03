package com.project.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.core.model.model.Source

@Database(entities = [Source::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}