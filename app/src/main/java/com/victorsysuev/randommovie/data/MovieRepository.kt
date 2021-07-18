package com.victorsysuev.randommovie.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.database.MovieLocalCache
import com.victorsysuev.randommovie.models.Cast
import com.victorsysuev.randommovie.network.Api
import ru.arston.randommovie.BuildConfig

class MovieRepository(
    private val apiService: Api,
    private val cache: MovieLocalCache
) {

    fun getMovies(): LiveData<PagedList<Movie>> {
        val dataSourceFactory = cache.getMovies()
        val boundaryCallback = MovieBoundaryCallback(apiService, cache)

        return LivePagedListBuilder(
            dataSourceFactory,
            DATABASE_PAGE_SIZE
        )
            .setBoundaryCallback(boundaryCallback)
            .build()
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

    suspend fun getCast(id: String): List<Cast>? {
        return apiService.getMovieCast(id, BuildConfig.TMDB_API_KEY).castList
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 30
    }

}