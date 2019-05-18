package ru.arston.randommovie.API

import com.example.randommovie.Models.MovieDetails
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

    @GET("discover/movie?language=ru-Ru")
    fun getAllMovie(@Query("with_genres") genre: Int,
                    @Query("primary_release_year") year: String,
                    @Query("region") region: String,
                    @Query("api_key") api_key: String): Deferred<Response<Movie>>

//    @GET("discover/movie?language=ru-Ru")
//    fun getRandomMovie(@Query("with_genres") genre: Int,
//                 @Query("primary_release_year") year: Int,
//                 @Query("page") page: Int,
//                 @Query("api_key") api_key: String): Call<Movie>

    @GET("movie/popular?language=ru-Ru")
    fun getPopularMovie(@Query("api_key") api_key: String): Deferred<Response<Movie>>

    @GET("movie/{movie_id}?language=ru-Ru")
    fun getMovie(@Path("movie_id") movie_id: String, @Query("api_key") api_key: String): Deferred<Response<MovieDetails>>

    @GET("discover/movie?language=ru-Ru")
    fun getRandomMovie(@Query("with_genres") genre: Int,
                       @Query("primary_release_year") year: String,
                       @Query("page") page: Int,
                       @Query("region") region: String,
                       @Query("api_key") api_key: String): Deferred<Response<Movie>>
}