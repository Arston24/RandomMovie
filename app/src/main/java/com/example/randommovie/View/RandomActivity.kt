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
import com.example.randommovie.Adapters.MovieAdapter
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
    var coordinatesCardView: IntArray = intArrayOf(1, 2)

    val genreArray = ArrayList<String>()
    val years = ArrayList<String>()
    val genreAll = mutableMapOf<Int, Int>()
    var genreID: Int = 0
    lateinit var movieYear: String

    lateinit var buttonRandom: Button
    lateinit var cardView: CardView
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var apiService: Api = retrofit.create(Api::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_random, container, false)
        buttonRandom = view.findViewById(R.id.button_random)
        cardView = view.findViewById(R.id.card_view)
        buttonRandom.setOnClickListener {
            cardView.visibility = view.visibility
            cardView.getLocationOnScreen(coordinatesCardView)
            buttonRandom.animate().translationY(cardView.translationY + 620)

            getMovie()
        }


        apiService.getGenres(apiKey).enqueue(object : Callback<Genre> {
            override fun onFailure(call: Call<Genre>, t: Throwable) {

            }

            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                genreList = response.body()?.genres!!
                for (i in genreList.indices) {
                    genreAll[i] = genreList[i].id!!
                    genreArray.add(genreList[i].name!!)
                }
                spinnerAdapter(genreArray)
            }
        })


        for (i in 1895..2019) years.add(i.toString())
        years.reverse()
        //years.add(0,"Год")
        val spinnerYear: Spinner = view!!.findViewById(R.id.spinner_year)
        spinnerYear.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, years)
        spinnerYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                movieYear = spinnerYear.selectedItem as String
            }
        }


        return view
    }


    private fun getMovie() {

        GlobalScope.launch(Dispatchers.Main) {
            val allMovieRequest = apiService.getAllMovie(genreID, movieYear, "US", apiKey)
            try {
                val response = allMovieRequest.await()
                if (response.isSuccessful) {
                    page = (1..response.body()?.totalPages!!).random()


                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }

            Log.e("genre", genreID.toString())
            Log.e("year", movieYear)
            Log.e("page", page.toString())
            Log.e("apiKey", apiKey)
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

        }

    }

    fun spinnerAdapter(list: ArrayList<String>) {

        val spinnerGenre: Spinner = view!!.findViewById(R.id.spinner_genre)
        spinnerGenre.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)

        spinnerGenre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genreID = genreAll[spinnerGenre.selectedItemPosition]!!
            }
        }
    }

    private fun setAdapter() {
        val r = (0..movieList.size).random()
        imageView = view!!.findViewById(R.id.card_image)
        textView = view!!.findViewById(R.id.card_title)
        Glide.with(this).load(imageUrl + movieList[r].posterPath).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(imageView)
        textView.text = movieList[r].title

        cardView.setOnClickListener {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("MovieID", movieList[r].id.toString())
            intent.putExtra("title", movieList[r].title)
            activity?.startActivity(intent)
        }
    }


}


