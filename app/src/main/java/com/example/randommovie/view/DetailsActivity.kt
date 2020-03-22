package com.example.randommovie

import androidx.room.Room
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieDatabase
import com.example.randommovie.models.Cast
import com.example.randommovie.view.PersonActivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.randommovie.network.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import kotlinx.android.synthetic.main.activity_details.*
import ru.arston.randommovie.databinding.ActivityDetailsBinding


class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    private var pressed: Boolean = false

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    lateinit var movieList: List<Movie>
    private var castList: List<Cast.Result> = listOf()
    private lateinit var movieID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        movieID = intent.extras?.getString("MovieID").toString()
        title = intent.extras?.getString("title")

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "descriptionMovie")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        movieList = db.movieDao().getAll()

        if (movieList.isNotEmpty()) {
            for (i in movieList.indices) {
                if (movieList[i].title == title) {
                    pressed = true
                    binding.bookmarkIcon.setOnClickListener {
                        pressed = false
                        binding.bookmarkIcon.setImageResource(R.mipmap.bookmark_unpressed)
                        Snackbar.make(
                            binding.bookmarkIcon, "Удалено из избранного",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        db.movieDao().deleteById(movieList[i].id)
                    }

                    binding.bookmarkIcon.setImageResource(R.mipmap.bookmark_pressed)
                    binding.movieDetailsTitle.text = movieList[i].title
                    binding.movieDetailsReleaseDate.text = movieList[i].releaseDate
                    binding.movieDetailsGenres.text = movieList[i].genre
                    binding.movieDetailsRating.rating = (movieList[i].voteAverage!!.div(2)).toFloat()
                    Glide.with(this@DetailsActivity)
                        .load("http://image.tmdb.org/t/p/w500" + movieList[i].backdropPath).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(binding.movieDetailsBackdrop)
                    binding.movieDetailsOverview.text = movieList[i].overview

                }
            }
        }
        getMovie()
    }

    /**
     * Метод отправляет запрос на сервер и получает необходимый фильм
     *
     */

    private fun getMovie() {

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        var apiService: Api = retrofit.create(Api::class.java)

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "descriptionMovie")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        GlobalScope.launch(Dispatchers.Main) {

            val detailsMovie = apiService.getMovie(movieID, apiKey)
            try {
                val response = detailsMovie.await()
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    binding.movieDetailsTitle.text = movieResponse?.title
                    binding.movieDetailsGenres.text = movieResponse?.genres?.get(0)!!.name
                    binding.movieDetailsReleaseDate.text = movieResponse.release_date
                    binding.movieDetailsRating.rating = (movieResponse.vote_average / 2).toFloat()
                    Glide.with(this@DetailsActivity)
                        .load("http://image.tmdb.org/t/p/w500" + movieResponse.backdrop_path).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(binding.movieDetailsBackdrop)
                    if (movieResponse.overview.isNotEmpty()) {
                        binding.summaryLabel.visibility = View.VISIBLE
                        binding.movieDetailsOverview.text = movieResponse.overview
                    }

                    // Если нажата кнопка "Добавить в избранное", то выполняется одно из действий:
                    // добавление в базу данных или удаление из неё
                    binding.bookmarkIcon.setOnClickListener {
                        if (!pressed) {
                            pressed = true
                            binding.bookmarkIcon.setImageResource(R.mipmap.bookmark_pressed)
                            Snackbar.make(
                                binding.bookmarkIcon, "Добавлено в избранное",
                                Snackbar.LENGTH_SHORT
                            ).show()

//                            db.movieDao().insert(listOf(movieResponse))

                        } else {
                            Snackbar.make(
                                binding.bookmarkIcon, "Удалено из избранного",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            pressed = false
                            binding.bookmarkIcon.setImageResource(R.mipmap.bookmark_unpressed)
                            db.movieDao().deleteById(movieResponse.id)
                        }
                    }

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }

            val castMovie = apiService.getMovieCast(movieID, apiKey)
            try {
                val response = castMovie.await()
                if (response.isSuccessful) {
                    val castLabel = findViewById<TextView>(R.id.castLabel)
                    castList = response.body()?.result!!
                    if (castList.isNotEmpty()) {
                        castLabel.visibility = View.VISIBLE

                        for (i in castList.indices) {
                            var parent: View = layoutInflater.inflate(R.layout.cast_item, movieCast, false)
                            var photoCast: ImageView = parent.findViewById(R.id.cast_photo)
                            var nameCast: TextView = parent.findViewById(R.id.cast_name)
                            var characterCast: TextView = parent.findViewById(R.id.cast_character)
                            nameCast.text = castList[i].name
                            characterCast.text = castList[i].character

                            Glide.with(this@DetailsActivity)
                                .load("http://image.tmdb.org/t/p/w500" + castList[i].profilePath)
                                .diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                ).into(photoCast)

                            photoCast.setOnClickListener {
                                val intent = Intent(this@DetailsActivity, PersonActivity::class.java)
                                intent.putExtra("PersonID", castList[i].id.toString())
                                this@DetailsActivity.startActivity(intent)
                            }
                            movieCast.addView(parent)
                            Log.e("MainActivity ", castList[i].id.toString())
                        }
                    }


                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }

        }
    }


}
