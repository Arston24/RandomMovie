package com.victorsysuev.randommovie.ui.adapters

import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.victorsysuev.randommovie.database.Movie
import com.victorsysuev.randommovie.ui.popular.TopMovieViewHolder
import com.victorsysuev.randommovie.ui.popular.TopMoviesFragmentDirections


class MovieAdapter : PagedListAdapter<Movie, TopMovieViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMovieViewHolder {
        return TopMovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TopMovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
            holder.viewModel.bind(it)
            holder.itemView.setOnClickListener {
                val action = TopMoviesFragmentDirections.actionTopMoviesFragmentToDetailsFragment2(movie.id.toString())
                Navigation.findNavController(holder.itemView).navigate(action)
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.equals(newItem)
        }
    }


}