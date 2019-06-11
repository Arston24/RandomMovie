package ru.arston.randommovie

import  android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.randommovie.View.Adapters.MovieAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.Models.Movie

@Suppress("SENSELESS_COMPARISON")
class TopActivity : Fragment() {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"

    private var movieList: MutableList<Movie.Result>? = null
    private lateinit var movieRecycler: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var page: Int = 0
    private var currentPage: Int = 1
    private var totalPage: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pastVisibleItemCount: Int = 0
    private var loading: Boolean = true


    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    var apiService: Api = retrofit.create(Api::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_top, container, false)

        movieRecycler = view.findViewById(R.id.movies_list)

        setupOnScrollListener()
        getMovies(currentPage)

        return view
    }

    fun getMovies(page: Int) {
        loading = true
        GlobalScope.launch(Dispatchers.Main) {
            val popularMoviePagesRequest = apiService.getPopularMoviePages(apiKey)
            try {
                val response = popularMoviePagesRequest.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    totalPage = movieResponse?.totalPages!!

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }

            val popularMovieRequest = apiService.getPopularMovie(page, apiKey)
            try {
                Log.e("MoviesRepository", "Current Page = $page")
                val response = popularMovieRequest.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()

                    if (movieList == null) {
                        movieList = movieResponse?.results!!
                        movieAdapter = MovieAdapter(movieList!!)
                        movieRecycler.adapter = movieAdapter
                    } else {
                        movieList!!.addAll(response.body()?.results!!)
                        val currentPosition = LinearLayoutManager(context).findLastVisibleItemPosition()
                        movieAdapter.notifyDataSetChanged()
                        movieRecycler.scrollToPosition(currentPosition)
                    }

                    currentPage = page
                    loading = false

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setupOnScrollListener() {
        val manager = LinearLayoutManager(context)
        movieRecycler.layoutManager = manager
        movieRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = manager.itemCount
                val visibleItemCount = manager.childCount
                val firstVisibleItem = manager.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (!loading) {
                        getMovies(currentPage + 1)
                        Snackbar.make(
                            movieRecycler,
                            "Загрузка фильмов...",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}