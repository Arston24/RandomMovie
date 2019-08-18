package com.example.randommovie.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Movie::class], version = 5)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

}