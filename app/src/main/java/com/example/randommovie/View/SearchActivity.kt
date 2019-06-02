package com.example.randommovie.View

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.randommovie.Adapters.MovieAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.Models.Movie
import ru.arston.randommovie.R
import ru.arston.randommovie.MainActivity
import android.support.v4.view.MenuItemCompat
import kotlinx.coroutines.*
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.view.inputmethod.InputMethodManager


class SearchActivity : AppCompatActivity() {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"

    lateinit var movieList: MutableList<Movie.Result>
    private lateinit var movieName: String
    lateinit var textResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setUpToolbar()
        //setSupportActionBar(toolbarSearch)


         //title = intent.extras.getString("MovieName")
         textResponse = findViewById(R.id.textResponse)
//        getMovie()
    }


    /**
     * Метод отправляет запрос на сервер
     * с названием фильма в качестве параметра запроса
     */
    private fun getMovie(title: String) {
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        var apiService: Api = retrofit.create(Api::class.java)

        val movieRecycler: RecyclerView = findViewById(R.id.movies_list)
        movieRecycler.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.Main) {
            val popularMovieRequest = apiService.getMovieSearch(title, apiKey)
            try {
                val response = popularMovieRequest.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()

                    movieList = movieResponse?.results!!
                    if (movieList.isNotEmpty()) {
                        val movieAdapter = MovieAdapter(movieList)
                        movieRecycler.adapter = movieAdapter
                    } else {
                        //textResponse.text = "Ничего не найдено!"

                    }

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }
        }

    }

    private fun setUpToolbar() {
        val mToolbar: Toolbar = findViewById(R.id.toolbarSearch)
        mToolbar.title = ""
        this.setSupportActionBar(mToolbar)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        searchItem?.expandActionView()
        val searchMenuItem = menu?.findItem(R.id.search)


        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

        var searchJob: Job? = null

        searchMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do whatever you need
                return true // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                onBackPressed()
                return false // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        })

        var searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
            private val searchJob: Job? = null


            override fun onQueryTextSubmit(title: String): Boolean {
                getMovie(title)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(title: String): Boolean {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    title?.let {
                        delay(500)
                        if (it.isEmpty()) {
                            textResponse.text = "Ничего не найдено!"
                        } else {
                            textResponse.text = ""
                            getMovie(title)
                        }
                    }
                }
                return false
            }

        }
        )

        return super.onCreateOptionsMenu(menu)
    }

}
