package com.example.randommovie.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ru.arston.randommovie.Models.Movie
import ru.arston.randommovie.R
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.randommovie.DetailsActivity


class MovieAdapter(private val movieList: List<Movie.Result>) : PagedListAdapter<Movie.Result, MovieAdapter.MovieViewHolder>(DIFF_UTIL) {

    class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val imageUrl: String = "http://image.tmdb.org/t/p/w500"
        var releaseDate: TextView? = view.findViewById(R.id.item_movie_release_date)
        var title: TextView = view.findViewById(R.id.item_movie_title)
        var rating: TextView = view.findViewById(R.id.item_movie_rating)
        var genres: TextView = view.findViewById(R.id.item_movie_genre)
        var poster: ImageView = view.findViewById(R.id.item_movie_poster)


        fun bind(movieList: Movie.Result) {
            releaseDate?.text = movieList.releaseDate!!.split("-")[0]
            title.text = movieList.title
            rating.text = movieList.voteAverage.toString()
            genres.text = ""
            Glide.with(view)
                .load(imageUrl + movieList.posterPath)
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(poster)
            view.setOnClickListener {
                val intent = Intent(view.context, DetailsActivity::class.java)
                intent.putExtra("MovieID", movieList.id.toString())
                intent.putExtra("title", movieList.title)
                view.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie.Result>() {
            override fun areItemsTheSame(oldItem: Movie.Result, newItem: Movie.Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie.Result, newItem: Movie.Result): Boolean =
                oldItem.equals(newItem)
        }
    }
}