package com.victorsysuev.randommovie


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.database.MovieDatabase
import com.victorsysuev.randommovie.database.MovieLocalCache
import com.victorsysuev.randommovie.models.Cast
import com.victorsysuev.randommovie.network.Api
import com.victorsysuev.randommovie.ui.details.CastAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.arston.randommovie.BuildConfig
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentDetailsBinding
import java.util.concurrent.Executors


class DetailsFragment : Fragment() {

    lateinit var binding: FragmentDetailsBinding

    lateinit var movieRepository: MovieRepository

    lateinit var castAdapter: CastAdapter
    private val apiKey: String = BuildConfig.TMDB_API_KEY
    private val url = "https://api.themoviedb.org/3/"
    lateinit var movieList: List<Movie>
    private var castList: ArrayList<Cast> = arrayListOf()
    lateinit var movie: Movie

    private var statusBarColor = 0

    val args: DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        val movieId = args.movieId

        NavigationUI.setupWithNavController(binding.toolbar, findNavController())

        statusBarColor = activity?.window?.statusBarColor ?: 0
        activity?.window?.statusBarColor = Color.TRANSPARENT

        castAdapter = CastAdapter()
        binding.castList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val db =
            Room.databaseBuilder(requireContext(), MovieDatabase::class.java, "descriptionMovie")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        movieList = db.movieDao().getAll()

        movieRepository = MovieRepository(
            Api.create(),
            MovieLocalCache(db.movieDao(), Executors.newSingleThreadExecutor())
        )

        movieRepository.getMovieById(movieId.toInt() ?: 0).observe(viewLifecycleOwner, Observer {
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
            movieId.let {
                movieRepository.getCast(movieId.toString())?.let {
                    binding.castLabel.visibility = View.VISIBLE
                    castList.addAll(it)
                    castAdapter.castList = castList
                    binding.castList.adapter = castAdapter
                }
            }
        }
        return binding.root
    }


    fun bind(movie: Movie) {
        binding.movieDetailsTitle.text = movie.title
        binding.movieDetailsGenres.text = movie.genre
        binding.movieDetailsReleaseDate.text = movie.releaseDate
        binding.movieDetailsRating.rating = movie.voteAverage?.div(2)?.toFloat() ?: 0f
        binding.movieDetailsOverview.text = movie.overview

        if (movie.isFavorite) {
            context?.let {
                binding.bookmarkIcon.background =
                    ContextCompat.getDrawable(it, R.drawable.bookmark_pressed_web)
            }
        } else {
            context?.let {
                binding.bookmarkIcon.background =
                    ContextCompat.getDrawable(it, R.drawable.bookmark_unpressed_web)
            }

        }

        Glide.with(this@DetailsFragment)
            .load("http://image.tmdb.org/t/p/w500" + movie.backdropPath)
            .diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.movieDetailsBackdrop)
    }

    override fun onDestroy() {
        activity?.window?.statusBarColor = statusBarColor
        super.onDestroy()
    }

}
