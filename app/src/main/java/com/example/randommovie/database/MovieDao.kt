package com.example.randommovie.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getMovies(): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>)

    @Query("DELETE FROM movies WHERE id=:id")
    fun deleteById(id: Int)
}