package com.example.randommovie.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getMovieById(id: Int): LiveData<Movie>

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getMovies(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movies WHERE isFavorite=1")
    fun getFavoriteMovies(): LiveData<List<Movie>>

//    @Query("UPDATE movies SET isFavorite=1 WHERE id=:id")
//    fun addToFavorite(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(movie: Movie)

    @Query("UPDATE movies SET isFavorite=0 WHERE id=:id")
    fun removeFromFavorite(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>)

    @Query("DELETE FROM movies WHERE id=:id")
    fun deleteById(id: Int)
}