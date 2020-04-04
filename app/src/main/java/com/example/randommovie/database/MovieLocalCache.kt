package com.example.randommovie.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import java.util.concurrent.Executor

class MovieLocalCache(
    private val movieDao: MovieDao,
    private val ioExecutor: Executor
) {
    fun insert(movies: List<Movie>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            movieDao.insert(movies)
            insertFinished()
        }
    }

    fun getMovies(): DataSource.Factory<Int, Movie> {
        return movieDao.getMovies()
    }

    fun getMovieById(id: Int): LiveData<Movie> {
        return movieDao.getMovieById(id)
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> {
        return movieDao.getFavoriteMovies()
    }

    fun addToFavorite(movie: Movie) {
        movieDao.addToFavorite(movie)
    }

    fun removeFromFavorite(id: Int) {
        movieDao.removeFromFavorite(id)
    }
}