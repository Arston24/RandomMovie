package com.victorsysuev.randommovie.ui.details


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.victorsysuev.randommovie.App
import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.models.Cast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentDetailsBinding
import javax.inject.Inject


class DetailsFragment : Fragment() {

    var binding: FragmentDetailsBinding? = null

    @Inject
    lateinit var movieRepository: MovieRepository

    lateinit var castAdapter: CastAdapter
    private var castArray: ArrayList<Cast> = arrayListOf()
    lateinit var movie: Movie
    private var statusBarColor = 0

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        val movieId = args.movieId

        binding?.apply {
            NavigationUI.setupWithNavController(toolbar, findNavController())

            statusBarColor = activity?.window?.statusBarColor ?: 0
            activity?.window?.statusBarColor = Color.TRANSPARENT

            castAdapter = CastAdapter()
            castList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            movieRepository.getMovieById(movieId.toInt())
                .observe(viewLifecycleOwner, {
                    if (it != null) {
                        bind(it)
                        movie = it
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            movieRepository.getMovie(movieId)?.let { response ->
                                movie = response
                                bind(response)
                            }
                        }
                    }
                })

            bookmarkIcon.setOnClickListener {
                if (movie.isFavorite) {
                    Snackbar.make(
                        bookmarkIcon, getString(R.string.removed_from_favorite),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    movieRepository.removeFromFavorite(movie.id)

                } else {
                    Snackbar.make(
                        bookmarkIcon, getString(R.string.added_to_favorite),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    movie.isFavorite = true
                    movieRepository.addToFavorite(movie)
                }
            }


            CoroutineScope(Dispatchers.Main).launch {
                movieId.let {
                    movieRepository.getCast(movieId)?.let {
                        castLabel.visibility = View.VISIBLE
                        castArray.addAll(it)
                        castAdapter.castList = castArray
                        castList.adapter = castAdapter
                    }
                }
            }
        }
        return binding!!.root
    }


    private fun bind(movie: Movie) {
        binding?.apply {
            movieDetailsTitle.text = movie.title
            movieDetailsGenres.text = movie.genre
            movieDetailsReleaseDate.text = movie.releaseDate
            movieDetailsRating.rating = movie.voteAverage?.div(2)?.toFloat() ?: 0f
            movieDetailsOverview.text = movie.overview

            if (movie.isFavorite) {
                context?.let {
                    bookmarkIcon.background =
                        ContextCompat.getDrawable(it, R.drawable.bookmark_pressed_web)
                }
            } else {
                context?.let {
                    bookmarkIcon.background =
                        ContextCompat.getDrawable(it, R.drawable.bookmark_unpressed_web)
                }

            }

            Glide.with(this@DetailsFragment)
                .load("http://image.tmdb.org/t/p/w500" + movie.backdropPath)
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).into(movieDetailsBackdrop)
        }
    }

    override fun onDestroy() {
        activity?.window?.statusBarColor = statusBarColor
        binding = null
        super.onDestroy()
    }

}
