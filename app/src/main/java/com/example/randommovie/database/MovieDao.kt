package com.example.randommovie.database

import androidx.room.*


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Insert
    fun insertAll(vararg movie: Movie)

    @Delete
    fun delete(movie: Movie)
}