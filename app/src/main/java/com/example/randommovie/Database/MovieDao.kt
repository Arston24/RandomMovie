package com.example.randommovie.Database

import android.arch.persistence.room.*


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Insert
    fun insertAll(vararg movie: Movie)

    @Delete
    fun delete(movie: Movie)
}