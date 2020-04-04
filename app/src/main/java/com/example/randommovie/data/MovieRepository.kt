package com.example.randommovie.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.network.Api
import ru.arston.randommovie.BuildConfig

class MovieRepository(
    private val apiService: Api,
    private val cache: MovieLocalCache
) {

    fun getMovies(): LiveData<PagedList<Movie>> {
        val dataSourceFactory = cache.getMovies()
        val boundaryCallback = MovieBoundaryCallback(apiService, cache)

        val data = LivePagedListBuilder(
            dataSourceFactory,
            DATABASE_PAGE_SIZE
        )
            .setBoundaryCallback(boundaryCallback)
            .build()

        return data
    }

    fun getMovieById(id: Int): LiveData<Movie> {
        return cache.getMovieById(id)
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> {
        return cache.getFavoriteMovies()
    }

    suspend fun getMovie(id: String): Movie? {
        return apiService.getMovie(id, BuildConfig.TMDB_API_KEY)
    }

    fun addToFavorite(movie: Movie) {
        cache.addToFavorite(movie)
    }

    fun removeFromFavorite(id: Int) {
        cache.removeFromFavorite(id)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

}