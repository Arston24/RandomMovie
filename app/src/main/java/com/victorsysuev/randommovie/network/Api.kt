package com.victorsysuev.randommovie.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.models.CastResponse
import com.victorsysuev.randommovie.models.Person
import com.victorsysuev.randommovie.models.PersonsMovie
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.Models.Genre
import ru.arston.randommovie.Models.MovieResponse


suspend fun getMovies(
    service: Api,
    page: Int,
    onSuccess: (repos: List<Movie>) -> Unit
) {
    onSuccess(service.getPopularMovies(page, BuildConfig.TMDB_API_KEY).movies ?: listOf())
}

interface Api {

    @GET("genre/movie/list?language=ru-Ru")
    fun getGenres(@Query("api_key") api_key: String): Call<Genre>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getAllMovie(
        @Query("with_genres") genre: Int,
        @Query("primary_release_year") year: String,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieResponse>>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getAllMovieWithoutGenre(
        @Query("primary_release_year") year: String,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieResponse>>


    @GET("movie/popular?language=ru-Ru")
    fun getPopularMoviePages(@Query("api_key") api_key: String): Deferred<Response<List<MovieResponse>>>

    @GET("movie/popular?language=ru-Ru")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") api_key: String
    ): MovieResponse

    @GET("movie/{movie_id}?language=ru-Ru")
    suspend fun getMovie(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): Movie

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getRandomMovie(
        @Query("with_genres") genre: Int,
        @Query("primary_release_year") year: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieResponse>>

    @GET("discover/movie?language=ru-Ru&vote_average.gte=7")
    fun getWithoutGenre(
        @Query("primary_release_year") year: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieResponse>>

    @GET("search/movie?language=ru-Ru")
    fun getMovieSearch(
        @Query("query") query: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieResponse>>

    @GET("movie/{movie_id}/credits?language=ru-Ru")
    suspend fun getMovieCast(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): CastResponse

    @GET("person/{person_id}?language=ru-Ru")
    fun getPersonDetails(
        @Path("person_id") person_id: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<Person>>

    @GET("person/{person_id}/combined_credits")
    fun getPersonMovie(
        @Path("person_id") person_id: String,
        @Query("api_key") api_key: String
    ): Deferred<Response<PersonsMovie>>


    @GET("authentication/token/new")
    fun getRequestToken(@Query("api_key") api_key: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Query("api_key") api_key: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("request_token") request_token: String
    )

    @FormUrlEncoded
    @POST("authentication/session/new")
    fun createSession(
        @Query("api_key") api_key: String,
        @Field("request_token") request_token: String
    ): Call<ResponseBody>


    companion object {
        fun create(): Api {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(Api::class.java)
        }
    }

}