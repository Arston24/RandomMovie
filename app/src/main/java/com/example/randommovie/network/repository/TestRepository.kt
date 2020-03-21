package com.example.randommovie.network.repository

import com.example.randommovie.network.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.Models.Movie

class TestRepository {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    var movie: List<Movie.Result> = listOf()

//    fun  getMovies(page: Int) = liveData(Dispatchers.IO) {
//        val topMovie = Api.create().getPopularMovie(page, apiKey).await().body()?.results
//        emit(topMovie)
//    }

    fun getMovies(
        service: Api,
        page: Int,
        onSuccess: (repos: List<com.example.randommovie.database.Movie>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        val response = service.getPopularMovie(page, apiKey)
    }
}