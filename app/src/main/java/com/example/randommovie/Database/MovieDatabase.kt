package com.example.randommovie.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = [Movie::class], version = 5)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

}