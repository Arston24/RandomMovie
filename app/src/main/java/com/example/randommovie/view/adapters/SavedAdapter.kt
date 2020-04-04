package com.example.randommovie.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.arston.randommovie.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.randommovie.database.Movie
import com.example.randommovie.DetailsActivity


class SavedAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<SavedAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


        private val imageUrl: String = "http://image.tmdb.org/t/p/w500"
        var poster: ImageView = view.findViewById(R.id.item_movie_saved)

        fun bind(movieList: Movie) {
            Glide.with(view)
                .load(imageUrl + movieList.posterPath)
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(poster)


            view.setOnClickListener {
                val intent = Intent(view.context, DetailsActivity::class.java)
                intent.putExtra("movieId", movieList.id.toString())
                intent.putExtra("title", movieList.title)
                view.context.startActivity(intent)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAdapter.MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_saved_card, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size
}