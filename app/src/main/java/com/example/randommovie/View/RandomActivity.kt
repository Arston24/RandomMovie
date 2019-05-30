package ru.arston.randommovie

import  android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.Models.Genre
import ru.arston.randommovie.Models.Movie
import java.util.*
import android.content.Intent
import com.example.randommovie.DetailsActivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RandomActivity : Fragment() {
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    private val imageUrl = "http://image.tmdb.org/t/p/w500"

    lateinit var genreList: List<Genre.Attributes>
    lateinit var movieList: List<Movie.Result>
    var page: Int = 0
    lateinit var ratingMovie: RatingBar

    val years = ArrayList<String>()
    val genreAll = mutableMapOf<Int, Int>()
    var genreID: Int = 0
    var movieYear: String = "0"

    lateinit var buttonRandom: Button
    lateinit var cardView: CardView
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var apiService: Api = retrofit.create(Api::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_random, container, false)

        buttonRandom = view.findViewById(R.id.button_random)
        cardView = view.findViewById(R.id.card_view)
        ratingMovie = view.findViewById(R.id.movieRating)

        buttonRandom.setOnClickListener {
            getMovie()
        }

        apiService.getGenres(apiKey).enqueue(object : Callback<Genre> {
            override fun onFailure(call: Call<Genre>, t: Throwable) {

            }

            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                genreList = response.body()?.genres!!
                var j = 1
                for (i in genreList.indices) {
                    genreAll[j] = genreList[i].id!!
                    j++
                }
                spinnerAdapter()
            }
        })

        years.clear()
        for (i in 1895..2019) years.add(i.toString())
        years.reverse()
        years.add(0, "Год")
        val spinnerYear: Spinner = view!!.findViewById(R.id.spinner_year)
        spinnerYear.adapter = ArrayAdapter(context, R.layout.item_spinner, years)
        spinnerYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                movieYear = spinnerYear.selectedItem as String
            }
        }

        getMovie()

        return view
    }

    /**
     * Метод отправляет запрос на сервер и, получив все фильмы,
     * генерирует номер случайной Json-страницы (page) из всех полученных, затем отправлет новый запрос,
     * передав в качестве одного из параметров запроса номер сгенерированной страницы.
     */
    private fun getMovie() {

        GlobalScope.launch(Dispatchers.Main) {
            // Если выбран какой-либо жанр
            if (genreID != 0) {
                val allMovieRequest = apiService.getAllMovie(genreID, movieYear, "US", apiKey)
                try {
                    val response = allMovieRequest.await()
                    if (response.isSuccessful) {
                        if (response.body()?.totalPages!! <= 1000) {
                            page = (1..response.body()?.totalPages!!).random()
                        } else page = (1..1000).random()


                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                    }
                } catch (e: Exception) {

                }

                val randomMovieRequest = apiService.getRandomMovie(genreID, movieYear, page, "US", apiKey)
                try {
                    val response = randomMovieRequest.await()
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        movieList = movieResponse?.results!!
                        setAdapter()

                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                    }
                } catch (e: Exception) {

                }
                // Если жанр не выбран
            } else {
                val allMovieRequest = apiService.getAllMovieWithoutGenre(movieYear, "US", apiKey)
                try {
                    val response = allMovieRequest.await()
                    if (response.isSuccessful) {
                        if (response.body()?.totalPages!! <= 1000) {
                            page = (1..response.body()?.totalPages!!).random()
                        } else page = (1..1000).random()


                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                    }
                } catch (e: Exception) {

                }

                val movieRequest = apiService.getWithoutGenre(movieYear, page, "US", apiKey)
                try {
                    val response = movieRequest.await()
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        movieList = movieResponse?.results!!
                        setAdapter()

                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    fun spinnerAdapter() {

        val spinnerGenre: Spinner = view!!.findViewById(R.id.spinner_genre)
        spinnerGenre.adapter =
            ArrayAdapter.createFromResource(context, R.array.movie_genres, R.layout.item_spinner)

        spinnerGenre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    genreID = 0
                } else genreID = genreAll[position]!!
            }
        }
    }

    /**
     * Функция заполняет данными CardView
     */
    private fun setAdapter() {
        val r = (0..movieList.size).random()
        imageView = view!!.findViewById(R.id.card_image)
        textView = view!!.findViewById(R.id.card_title)
        Glide.with(this).load(imageUrl + movieList[r].posterPath).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(imageView)
        textView.text = movieList[r].title
        ratingMovie.rating = (movieList[r].voteAverage!! / 2).toFloat()

        cardView.setOnClickListener {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("MovieID", movieList[r].id.toString())
            intent.putExtra("title", movieList[r].title)
            activity?.startActivity(intent)
        }
    }
}


