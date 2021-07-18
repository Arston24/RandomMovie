package com.victorsysuev.randommovie.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorsysuev.randommovie.App
import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.ui.adapters.MovieAdapter
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentTopBinding
import javax.inject.Inject


class TopMoviesFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding

    @Inject
    lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container, false)
        binding.moviesList.layoutManager = LinearLayoutManager(context)
        getMovies()
        return binding.root
    }

    private fun getMovies() {
        val movieAdapter = MovieAdapter()
        binding.moviesList.adapter = movieAdapter
        movieRepository.getMovies().observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })
    }
}
