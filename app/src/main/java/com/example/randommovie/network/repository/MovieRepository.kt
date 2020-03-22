package com.example.randommovie.network.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.randommovie.data.MovieBoundaryCallback
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.network.Api

class MovieRepository(
    private val apiService: Api,
    private val cache: MovieLocalCache
) {

    fun getMovies(): LiveData<PagedList<Movie>> {
        val dataSourceFactory = cache.getMovies()
        val boundaryCallback = MovieBoundaryCallback(apiService, cache)

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return data
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

}