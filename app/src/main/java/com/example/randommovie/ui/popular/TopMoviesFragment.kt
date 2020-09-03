package com.example.randommovie.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.randommovie.database.MovieDatabase
import com.example.randommovie.database.MovieLocalCache
import com.example.randommovie.network.Api
import com.example.randommovie.data.MovieRepository
import com.example.randommovie.ui.adapters.MovieAdapter
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentTopBinding
import java.util.concurrent.Executors


class TopMoviesFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding

    lateinit var movieRepository: MovieRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container, false)
        binding.moviesList.layoutManager = LinearLayoutManager(context)
        val db = Room.databaseBuilder(
            requireContext(),
            MovieDatabase::class.java,
            "descriptionMovie"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        movieRepository = MovieRepository(
            Api.create(),
            MovieLocalCache(db.movieDao(), Executors.newSingleThreadExecutor())
        )
        getMovies()
        return binding.root
    }

    private fun getMovies() {
        val movieAdapter = MovieAdapter()
        binding.moviesList.adapter = movieAdapter
        movieRepository.getMovies().observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })
    }
}
