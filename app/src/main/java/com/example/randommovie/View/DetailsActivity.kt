package com.example.randommovie

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.Database.Movie
import com.example.randommovie.Database.MovieDatabase
import com.example.randommovie.Models.Cast
import com.example.randommovie.Models.MovieDetails
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arston.randommovie.API.Api
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {
    lateinit var titleText: TextView
    lateinit var genresText: TextView
    lateinit var dateReleaseText: TextView
    lateinit var overviewText: TextView
    lateinit var reviewsText: TextView
    lateinit var ratingMovie: RatingBar
    lateinit var backdropImage: ImageView
    lateinit var bookmarkIcon: ImageView
    private var pressed: Boolean = false
    lateinit var labelSummary: TextView

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    lateinit var movieList: List<Movie>
    private var castList: List<Cast.Result> = listOf()
    private lateinit var movieID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        titleText = findViewById(R.id.movieDetailsTitle)
        genresText = findViewById(R.id.movieDetailsGenres)
        dateReleaseText = findViewById(R.id.movieDetailsReleaseDate)
        overviewText = findViewById(R.id.movieDetailsOverview)
        ratingMovie = findViewById(R.id.movieDetailsRating)
        backdropImage = findViewById(R.id.movieDetailsBackdrop)
        bookmarkIcon = findViewById(R.id.bookmark_icon)
        movieID = intent.extras.getString("MovieID")
        title = intent.extras.getString("title")
        labelSummary = findViewById(R.id.summaryLabel)


        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "descriptionMovie")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        movieList = db.movieDao().getAll()


        // Если база данных содержит какие-либо фильмы,
        // то проверяется есть ли необходимый фильм среди них
        if (movieList.isNotEmpty()) {
            for (i in movieList.indices) {
                if (movieList[i].title == title) {
                    pressed = true
                    bookmarkIcon.setOnClickListener {

                        pressed = false
                        bookmarkIcon.setImageResource(R.mipmap.bookmark_unpressed)
                        Snackbar.make(
                            bookmarkIcon, "Удалено из избранного",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        db.movieDao().delete(
                            Movie(
                                movieList[i].uid,
                                movieList[i].title,
                                movieList[i].posterPath,
                                movieList[i].backdropPath,
                                movieList[i].rating,
                                movieList[i].overview,
                                movieList[i].releaseDate,
                                movieList[i].genre
                            )
                        )

                    }

                    bookmarkIcon.setImageResource(R.mipmap.bookmark_pressed)
                    titleText.text = movieList[i].title
                    dateReleaseText.text = movieList[i].releaseDate
                    genresText.text = movieList[i].genre
                    ratingMovie.rating = (movieList[i].rating!!.div(2)).toFloat()
                    Glide.with(this@DetailsActivity)
                        .load("http://image.tmdb.org/t/p/w500" + movieList[i].backdropPath).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(backdropImage)
                    overviewText.text = movieList[i].overview


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
                    titleText.text = movieResponse?.title
                    genresText.text = movieResponse?.genres?.get(0)!!.name
                    dateReleaseText.text = movieResponse.release_date
                    ratingMovie.rating = (movieResponse.vote_average / 2).toFloat()
                    Glide.with(this@DetailsActivity)
                        .load("http://image.tmdb.org/t/p/w500" + movieResponse.backdrop_path).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).into(backdropImage)
                    if(movieResponse.overview.isNotEmpty()) {
                        labelSummary.visibility = View.VISIBLE
                        overviewText.text = movieResponse.overview
                    }


                    // Если нажата кнопка "Добавить в избранное", то выполняется одно из действий:
                    // добавление в базу данных или удаление из неё
                    bookmarkIcon.setOnClickListener {
                        if (!pressed) {
                            pressed = true
                            bookmarkIcon.setImageResource(R.mipmap.bookmark_pressed)
                            Snackbar.make(
                                bookmarkIcon, "Добавлено в избранное",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            db.movieDao().insertAll(
                                Movie(
                                    movieResponse.id,
                                    movieResponse.title,
                                    movieResponse.poster_path,
                                    movieResponse.backdrop_path,
                                    movieResponse.vote_average,
                                    movieResponse.overview,
                                    movieResponse.release_date,
                                    movieResponse.genres[0].name
                                )
                            )

                        } else {
                            Snackbar.make(
                                bookmarkIcon, "Удалено из избранного",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            pressed = false
                            bookmarkIcon.setImageResource(R.mipmap.bookmark_unpressed)
                            db.movieDao().delete(
                                Movie(
                                    movieResponse.id,
                                    movieResponse.title,
                                    movieResponse.poster_path,
                                    movieResponse.backdrop_path,
                                    movieResponse.vote_average,
                                    movieResponse.overview,
                                    movieResponse.release_date,
                                    movieResponse.genres[0].name
                                )
                            )
                        }
                    }

                } else {
                    Log.e("MainActivity ", response.errorBody().toString())
                }
            } catch (e: Exception) {

            }


            val castMovie = apiService.getMovieCast(movieID, apiKey)
            try {
                if (castList.isNullOrEmpty()) {
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
                                movieCast.addView(parent)
                                Log.e("MainActivity ", castList.indices.toString())
                            }
                        }


                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                    }
                }
            } catch (e: Exception) {


            }




        }
    }


}
