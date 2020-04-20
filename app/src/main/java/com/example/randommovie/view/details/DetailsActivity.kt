package com.example.randommovie

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.randommovie.data.MovieRepository
import com.example.randommovie.database.Movie
import com.example.randommovie.database.MovieDatabase
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.models.Cast
import com.example.randommovie.network.Api
import com.example.randommovie.view.details.CastAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.ActivityDetailsBinding
import timber.log.Timber
import java.util.concurrent.Executors


class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    lateinit var movieRepository: MovieRepository

    lateinit var castAdapter: CastAdapter
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    lateinit var movieList: List<Movie>
    private var castList: ArrayList<Cast> = arrayListOf()
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        val movieId = intent.extras?.getString("movieId")

        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        castAdapter = CastAdapter()
        binding.castList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
                    binding.bookmarkIcon, getString(R.string.removed_from_favorite),
                    Snackbar.LENGTH_SHORT
                ).show()
                movieRepository.removeFromFavorite(movie.id)

            } else {
                Snackbar.make(
                    binding.bookmarkIcon, getString(R.string.added_to_favorite),
                    Snackbar.LENGTH_SHORT
                ).show()
                movie.isFavorite = true
                movieRepository.addToFavorite(movie)
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            movieId?.let {
                movieRepository.getCast(movieId.toString())?.let {
                    binding.castLabel.visibility = View.VISIBLE
                    castList.addAll(it)
                    castAdapter.castList = castList
                    binding.castList.adapter = castAdapter
                }
            }
        }
    }


    fun bind(movie: Movie) {
        binding.movieDetailsTitle.text = movie.title
        binding.movieDetailsGenres.text = movie.genre
        binding.movieDetailsReleaseDate.text = movie.releaseDate
        binding.movieDetailsRating.rating = movie.voteAverage?.div(2)?.toFloat() ?: 0f
        binding.movieDetailsOverview.text = movie.overview

        if (movie.isFavorite) {
            binding.bookmarkIcon.background = getDrawable(R.drawable.bookmark_pressed_web)
        } else {
            binding.bookmarkIcon.background = getDrawable(R.drawable.bookmark_unpressed_web)
        }

        Glide.with(this@DetailsActivity)
            .load("http://image.tmdb.org/t/p/original" + movie.backdropPath)
            .diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.movieDetailsBackdrop)
    }
}
