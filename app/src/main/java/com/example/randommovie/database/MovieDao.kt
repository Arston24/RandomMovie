package com.example.randommovie.database

import androidx.paging.DataSource
import androidx.room.*


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies")
    fun getMovies(): DataSource.Factory<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movie: Movie)

    @Delete
    fun delete(movie: Movie)
}