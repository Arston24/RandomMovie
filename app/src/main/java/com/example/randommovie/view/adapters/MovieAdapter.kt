package com.example.randommovie.view.adapters

import android.content.Intent
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.randommovie.DetailsActivity
import com.example.randommovie.database.Movie
import com.example.randommovie.view.TopMovieViewHolder


class MovieAdapter : PagedListAdapter<Movie, TopMovieViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMovieViewHolder {
        return TopMovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TopMovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
            holder.viewModel.bind(it)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("MovieID", movie?.id)
            intent.putExtra("title", movie?.title)
            holder.itemView.context.startActivity(intent)
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