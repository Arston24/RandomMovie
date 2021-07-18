package com.victorsysuev.randommovie.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.network.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.Models.Genre
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentRandomBinding
import timber.log.Timber
import java.util.*


class RandomMoviesFragment : Fragment() {

    lateinit var binding: FragmentRandomBinding
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val imageUrl = "http://image.tmdb.org/t/p/w500"

    lateinit var genreList: List<Genre.Attributes>
    lateinit var movieResponseList: List<Movie>
    var page: Int = 0

    val years = ArrayList<String>()
    val genreAll = mutableMapOf<Int, Int>()
    var genreID: Int = 0
    var movieYear: String = "0"

    var apiService = Api.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_random, container, false)

        binding.buttonRandom.setOnClickListener {
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
        binding.spinnerYear.adapter =
            ArrayAdapter(binding.spinnerYear.context, R.layout.item_spinner, years)
        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                movieYear = binding.spinnerYear.selectedItem as String
            }
        }

        getMovie()

        return binding.root
    }

    private fun getMovie() {

        // Если выбран какой-либо жанр
        if (genreID != 0) {
            try {
                val allMovieRequest =
                    apiService.getAllMovies(genreID, movieYear, "US", apiKey).blockingGet()
                if (allMovieRequest.isSuccessful) {
                    if (allMovieRequest.body()?.totalPages!! <= 1000) {
                        page = (1..allMovieRequest.body()?.totalPages!!).random()
                    } else page = (1..1000).random()


                } else {
                    Timber.e(allMovieRequest.errorBody().toString())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            try {
                val randomMovieRequest =
                    apiService.getRandomMovie(genreID, movieYear, page, "US", apiKey).blockingGet()
                if (randomMovieRequest.isSuccessful) {
                    val movieResponse = randomMovieRequest.body()
                    movieResponseList = movieResponse?.movies!!
                    setAdapter()

                } else {
                    Timber.e(randomMovieRequest.errorBody().toString())
                }
            } catch (e: Exception) {

            }
            // Если жанр не выбран
        } else {
            val allMovieRequest =
                apiService.getAllMovieWithoutGenre(movieYear, "US", apiKey).blockingGet()
            try {
                if (allMovieRequest.isSuccessful) {
                    if (allMovieRequest.body()?.totalPages!! <= 1000) {
                        page = (1..allMovieRequest.body()?.totalPages!!).random()
                    } else page = (1..1000).random()


                } else {
                    Timber.e(allMovieRequest.errorBody().toString())
                }
            } catch (e: Exception) {

            }

            val movieRequest =
                apiService.getWithoutGenre(movieYear, page, "US", apiKey).blockingGet()
            try {
                if (movieRequest.isSuccessful) {
                    val movieResponse = movieRequest.body()
                    movieResponseList = movieResponse?.movies!!
                    setAdapter()

                } else {
                    Timber.e(movieRequest.errorBody().toString())
                }
            } catch (e: Exception) {

            }
        }
    }

    fun spinnerAdapter() {

        binding.spinnerGenre.adapter =
            ArrayAdapter.createFromResource(
                binding.spinnerGenre.context,
                R.array.movie_genres,
                R.layout.item_spinner
            )

        binding.spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    genreID = 0
                } else genreID = genreAll[position]!!
            }
        }
    }

    private fun setAdapter() {
        val r = (0..movieResponseList.size).random()
        Glide.with(this).load(imageUrl + movieResponseList[r].posterPath).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(binding.cardImage)
        binding.cardTitle.text = movieResponseList[r].title
        binding.movieRating.rating = (movieResponseList[r].voteAverage!! / 2).toFloat()

        binding.cardView.setOnClickListener {
            val action = RandomMoviesFragmentDirections.actionRandomFragmentToDetailsFragment(
                movieResponseList[r].id.toString()
            )
            findNavController().navigate(action)
        }
    }
}


