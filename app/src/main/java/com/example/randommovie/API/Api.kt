package ru.arston.randommovie.API

import com.example.randommovie.Models.Cast
import com.example.randommovie.Models.MovieDetails
import com.example.randommovie.Models.Person
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.arston.randommovie.Models.Genre
import ru.arston.randommovie.Models.Movie

interface Api {

    @GET("genre/movie/list?language=ru-Ru")
    fun getGenres(@Query("api_key") api_key: String): Call<Genre>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getAllMovie(
        @Query("with_genres") genre: Int,
        @Query("primary_release_year") year: String,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<Movie>>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getAllMovieWithoutGenre(
        @Query("primary_release_year") year: String,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<Movie>>


    @GET("movie/popular?language=ru-Ru")
    fun getPopularMovie(@Query("api_key") api_key: String): Deferred<Response<Movie>>

    @GET("movie/{movie_id}?language=ru-Ru")
    fun getMovie(@Path("movie_id") movie_id: String, @Query("api_key") api_key: String): Deferred<Response<MovieDetails>>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getRandomMovie(
        @Query("with_genres") genre: Int,
        @Query("primary_release_year") year: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<Movie>>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getWithoutGenre(
        @Query("primary_release_year") year: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<Movie>>


    @GET("search/movie?language=ru-Ru")
    fun getMovieSearch(@Query("query") query: String, @Query("api_key") api_key: String): Deferred<Response<Movie>>

    @GET("movie/{movie_id}/credits?language=ru-Ru")
    fun getMovieCast(@Path("movie_id") movie_id: String, @Query("api_key") api_key: String): Deferred<Response<Cast>>

    @GET("person/{person_id}?language=ru-Ru")
    fun getPersonDetails (@Path("person_id") person_id: String, @Query("api_key") api_key: String): Deferred<Response<Person>>

}