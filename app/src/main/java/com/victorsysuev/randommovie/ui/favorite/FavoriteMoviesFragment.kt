package com.victorsysuev.randommovie.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.victorsysuev.randommovie.data.MovieRepository
import com.victorsysuev.randommovie.database.MovieDatabase
import com.victorsysuev.randommovie.database.MovieLocalCache
import com.victorsysuev.randommovie.network.Api
import ru.arston.randommovie.databinding.FragmentFavoriteBinding
import java.util.concurrent.Executors


class FavoriteMoviesFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.favoriteList.layoutManager = GridLayoutManager(this.context, 3)

        val db =
            Room.databaseBuilder(requireContext(), MovieDatabase::class.java, "descriptionMovie")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

        val movieRepository = MovieRepository(
            Api.create(),
            MovieLocalCache(db.movieDao(), Executors.newSingleThreadExecutor())
        )

        movieRepository.getFavoriteMovies().observe(viewLifecycleOwner, Observer {
            adapter = FavoriteAdapter(it)
            binding.favoriteList.adapter = adapter
        })


        return binding.root
    }
}