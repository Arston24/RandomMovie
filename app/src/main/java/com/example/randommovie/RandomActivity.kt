package ru.arston.randommovie

import android.os.Bundle
import android.support.v4.app.Fragment
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



class RandomActivity : Fragment(){
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"

    lateinit var genreList: List<Genre.Attributes>
    lateinit var movieList: List<Movie.Result>
    var coordinatesCardView: IntArray =  intArrayOf(1, 2)

    val genreArray = ArrayList<String>()
    val years = ArrayList<Int>()
    val genreAll = mutableMapOf<Int, Int>()
    var genreID: Int = 0
    var movieYear: Int = 0

    lateinit var buttonRandom: Button
    lateinit var cardView: CardView
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
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

        for (i in 1895..2019) years.add(i)
        years.reverse()
        val spinnerYear: Spinner = view!!.findViewById(R.id.spinner_year)
        spinnerYear.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, years)
        spinnerYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                movieYear = spinnerYear.selectedItem as Int
            }
        }


        return view
    }
        private fun getMovie() {

            apiService.getMovie(genreID, movieYear, apiKey).enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    movieList = response.body()?.results!!
                   // Log.e("Response", "= " + movieList[0].title)
                    setAdapter()
                }
            })

        }

        fun spinnerAdapter(list: ArrayList<String>) {

            val spinnerGenre: Spinner = view!!.findViewById(R.id.spinner_genre)
            spinnerGenre.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)

            spinnerGenre?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    genreID = genreAll[spinnerGenre.selectedItemPosition]!!
                   // Log.e("ID", "= $genreID")

                }
            }
        }

         fun setAdapter(){
             val r =(0..19).random()
             movieList[1].video
             imageView = view!!.findViewById(R.id.card_image)
             textView = view!!.findViewById(R.id.card_title)
             Glide.with(this).load("http://image.tmdb.org/t/p/w185" + movieList[r].posterPath).diskCacheStrategy(
             DiskCacheStrategy.ALL).into(imageView)
             textView.text = movieList[r].title
    }
}


