package com.example.randommovie.database

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

    fun getFavoriteMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }
}