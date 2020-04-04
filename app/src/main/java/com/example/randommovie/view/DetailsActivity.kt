package com.example.randommovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.data.MovieRepository
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieDatabase
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.models.Cast
import com.example.randommovie.network.Api
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.ActivityDetailsBinding
import java.util.concurrent.Executors


class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    lateinit var movieRepository: MovieRepository

    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    lateinit var movieList: List<Movie>
    private var castList: List<Cast.Result> = listOf()
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        val movieId = intent.extras?.getString("movieId")
        val title = intent.extras?.getString("title")

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "descriptionMovie")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        movieList = db.movieDao().getAll()

        movieRepository = MovieRepository(
            Api.create(),
            MovieLocalCache(db.movieDao(), Executors.newSingleThreadExecutor())
        )

        movieRepository.getMovieById(movieId?.toInt() ?: 0).observe(this, Observer {
            if (it != null) {
                bind(it)
                movie = it
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    movieRepository.getMovie(movieId.toString())?.let { response ->
                        movie = response
                        bind(response)
                    }
                }
            }
        })

        binding.bookmarkIcon.setOnClickListener {
            if (movie.isFavorite) {
                Snackbar.make(
                    binding.bookmarkIcon, "Удалено из избранного",
                    Snackbar.LENGTH_SHORT
                ).show()
                movieRepository.removeFromFavorite(movie.id)

            } else {
                Snackbar.make(
                    binding.bookmarkIcon, "Добавлено в избранное",
                    Snackbar.LENGTH_SHORT
                ).show()
                movie.isFavorite = true
                movieRepository.addToFavorite(movie)
            }
        }
    }


    fun bind(movie: Movie) {
        binding.movieDetailsTitle.text = movie.title
        binding.movieDetailsGenres.text = movie.genre
        binding.movieDetailsReleaseDate.text = movie.releaseDate
        binding.movieDetailsRating.rating = movie.voteAverage?.div(2)?.toFloat() ?: 0f

        if (movie.isFavorite) {
            binding.bookmarkIcon.background = getDrawable(R.drawable.bookmark_pressed_web)
        } else {
            binding.bookmarkIcon.background = getDrawable(R.drawable.bookmark_unpressed_web)
        }

        Glide.with(this@DetailsActivity)
            .load("http://image.tmdb.org/t/p/w500" + movie.backdropPath)
            .diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.movieDetailsBackdrop)
    }
}
