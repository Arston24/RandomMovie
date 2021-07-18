package com.victorsysuev.randommovie.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.victorsysuev.randommovie.App
import com.victorsysuev.randommovie.data.MovieRepository
import ru.arston.randommovie.databinding.FragmentFavoriteBinding
import javax.inject.Inject


class FavoriteMoviesFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    lateinit var adapter: FavoriteAdapter

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
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.favoriteList.layoutManager = GridLayoutManager(this.context, 3)

        movieRepository.getFavoriteMovies().observe(viewLifecycleOwner, {
            adapter = FavoriteAdapter(it)
            binding.favoriteList.adapter = adapter
        })


        return binding.root
    }
}