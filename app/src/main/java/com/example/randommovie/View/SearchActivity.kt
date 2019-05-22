package com.example.randommovie.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.example.randommovie.Adapters.MovieAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.Models.Movie
import ru.arston.randommovie.R

class SearchActivity : AppCompatActivity() {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"

    lateinit var movieList: List<Movie.Result>
    private lateinit var movieName: String
    lateinit var textResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        movieName = intent.extras.getString("MovieName")
        textResponse = findViewById(R.id.textResponse)
        getMovie()
    }

    /**
     * Метод отправляет запрос на сервер
     * с названием фильма в качестве параметра запроса
     */
    private fun getMovie() {
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        var apiService: Api = retrofit.create(Api::class.java)

        val movieRecycler: RecyclerView = findViewById(R.id.movies_list)
        movieRecycler.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.Main) {
            val popularMovieRequest = apiService.getMovieSearch(movieName, apiKey)
            try {
                val response = popularMovieRequest.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()

                    movieList = movieResponse?.results!!
                    if (movieList.isNotEmpty()) {
                        val movieAdapter = MovieAdapter(movieList)
                        movieRecycler.adapter = movieAdapter
                    } else {
                        textResponse.text = "Ничего не найдено!"

                    }

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }
        }

    }

}
