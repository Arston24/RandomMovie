package ru.arston.randommovie

import  android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.randommovie.Adapters.MovieAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.Models.Movie

class TopActivity : Fragment() {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"

    lateinit var movieList: List<Movie.Result>


    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    var apiService: Api = retrofit.create(Api::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_top, container, false)

        val movieRecycler: RecyclerView = view.findViewById(R.id.movies_list)
        movieRecycler.layoutManager = LinearLayoutManager(context)

        // Отправка запроса на сервер и получение списка
        // самых популярных фильмов
        GlobalScope.launch(Dispatchers.Main) {
            val popularMovieRequest = apiService.getPopularMovie(apiKey)
            try {
                val response = popularMovieRequest.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    movieList = movieResponse?.results!!
                    val movieAdapter = MovieAdapter(movieList)
                    movieRecycler.adapter = movieAdapter

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }
        }
        return view
    }
}