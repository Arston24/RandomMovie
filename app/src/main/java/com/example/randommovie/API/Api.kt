package ru.arston.randommovie.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.arston.randommovie.Models.Genre
import ru.arston.randommovie.Models.Movie

interface Api {

    @GET("genre/movie/list?language=ru-Ru")
    fun getGenres(@Query("api_key") api_key: String): Call<Genre>

    @GET("discover/movie?language=ru-Ru")
    fun getMovie(@Query("with_genres") genre: Int,
                 @Query("primary_release_year") year: Int,
                 @Query("api_key") api_key: String): Call<Movie>
}